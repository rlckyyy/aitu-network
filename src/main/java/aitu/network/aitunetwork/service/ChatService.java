package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.dto.WSMessageType;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomsWithMessages;
import aitu.network.aitunetwork.model.dto.chat.MessageMark;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.WSMessage;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ReplaceRootOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;
    private final FileService fileService;

    public void processMessage(
            ChatMessage newChatMessage
    ) {
        ChatMessage chatMessage = chatMessageService.save(newChatMessage);
        ChatRoom chatRoom = chatRoomService.findChatRoom(chatMessage.getChatId());
        if (chatRoom.getEmpty()) {
            chatRoom = chatRoomService.markChatRoom(chatRoom, false);
        }

        WSMessage wsMessage = new WSMessage(WSMessageType.CHAT_MESSAGE, chatMessage);
        chatMessageService.sendWSMessageToUsers(chatRoom.getParticipants().stream().map(User::getId).toList(), wsMessage);
    }

    public Map<String, Object> countNewMessages(String chatId, User user) {
        return Map.of("count", chatMessageService.countNewMessages(chatId, user));
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageService.findChatMessages(chatId);
    }

    public List<ChatRoomsWithMessages> findChats(User user) {
        MatchOperation match = Aggregation.match(Criteria.where("participants.$id").in(new ObjectId(user.getId())));
        LookupOperation lookup = Aggregation.lookup(
                "chatMessage",
                ChatRoom.Fields.chatId,
                ChatMessage.Fields.chatId,
                ChatRoomsWithMessages.Fields.messages
        );
        ReplaceRootOperation rootReplacement = Aggregation.replaceRoot(
                context -> new Document(
                        "$mergeObjects", List.of(
                        new Document(ChatRoomsWithMessages.Fields.chatRoom, "$$ROOT"),
                        new Document(ChatRoomsWithMessages.Fields.messages, "$".concat(ChatRoomsWithMessages.Fields.messages))
                ))
        );

        Aggregation aggregation = Aggregation.newAggregation(match, lookup, rootReplacement);
        return mongoTemplate.aggregate(aggregation, ChatRoom.class, ChatRoomsWithMessages.class).getMappedResults();
    }

    public List<ChatRoomDTO> findUserChatRooms(User user) {
        return chatRoomService.findUserChatRooms(user);
    }


    public ChatRoomDTO createChatRoom(NewChatRoomDTO chatRoom, User user) {
        ChatRoomDTO dto = chatRoomService.createChatRoom(chatRoom, user);
        if (dto.chatRoomType().equals(ChatRoomType.GROUP)) {
            WSMessage wsMessage = new WSMessage(WSMessageType.CHAT_ROOM, dto);
            chatMessageService.sendWSMessageToUsers(dto.participants().stream().map(UserShortDTO::getId).toList(), wsMessage);
        }
        return dto;
    }

    public void addParticipantToChatRoom(String chatRoomId, String participantId) {
        chatRoomService.actionParticipantToChatRoom(chatRoomId, participantId, ChatRoom::addParticipant);
    }

    public void deleteUserFromChatRoom(String chatRoomId, String participantId) {
        chatRoomService.actionParticipantToChatRoom(chatRoomId, participantId, ChatRoom::removeParticipant);
    }

    public ChatMessage saveFileMessage(ChatMessage chatMessage, MultipartFile file) {
        if (!chatMessage.getType().isFileMessageType) {
            throw new IllegalArgumentException("Message type is non file type");
        }
        String fileId = fileService.uploadFile(file);
        String link = fileService.getLinkForResource(fileId);
        chatMessage.setContent(link);
        return chatMessageService.save(chatMessage);
    }

    public void processMessageStatus(MessageMark messageMark) {
        chatMessageService.markChatMessageAsRead(messageMark);
    }
}
