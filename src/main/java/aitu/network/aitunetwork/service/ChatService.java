package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.dto.ChatRoomsWithMessages;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.MessageMark;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final UserService userService;

    public void processMessage(
            ChatMessage newChatMessage
    ) {
        ChatMessage chatMessage = chatMessageService.save(newChatMessage);
        ChatRoom chatRoom = chatRoomService.findChatRoom(chatMessage.getChatId());
        if (chatRoom.getEmpty()) {
            chatRoom = chatRoomService.markChatRoom(chatRoom, false);
        }

        chatRoom.getParticipants().forEach(participant ->
                messagingTemplate.convertAndSendToUser(participant.getId(), "/queue/messages", chatMessage)
        );
    }

    public Map<String, Object> countNewMessages(String chatId, User user) {
        return Map.of("count", chatMessageService.countNewMessages(chatId, user));
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageService.findChatMessages(chatId);
    }

    public List<ChatRoomsWithMessages> findChats(User user) {
        Map<String, ChatRoomDTO> chatRooms = chatRoomService.findUserChatRooms(user).stream()
                .collect(Collectors.toMap(ChatRoomDTO::chatId, Function.identity()));

        Map<ChatRoomDTO, List<ChatMessage>> chatRoomsWithMessagesMap = chatMessageService.findChatMessages(chatRooms.keySet()).stream()
                .collect(Collectors.groupingBy(chatMessage -> chatRooms.get(chatMessage.getChatId())));

        return ChatRoomsWithMessages.fromMap(chatRoomsWithMessagesMap);
    }

    public List<ChatRoomDTO> findUserChatRooms(User user) {
        return chatRoomService.findUserChatRooms(user);
    }

    public List<User> searchUsers(String query, User user) {
        return userService.searchUsers(query, user);
    }

    public ChatRoomDTO createChatRoom(NewChatRoomDTO chatRoom, User user) {
        return chatRoomService.createChatRoom(chatRoom, user);
    }

    public void addParticipantToChatRoom(String chatRoomId, String participantId) {
        chatRoomService.actionParticipantToChatRoom(chatRoomId, participantId, ChatRoom::addParticipant);
    }

    public void deleteUserFromChatRoom(String chatRoomId, String participantId) {
        chatRoomService.actionParticipantToChatRoom(chatRoomId, participantId, ChatRoom::removeParticipant);
    }

    public ChatMessage saveFileMessage(ChatMessage chatMessage, MultipartFile audioFile) {
        return chatMessageService.saveFileMessage(chatMessage, audioFile);
    }

    public void processMessageStatus(MessageMark messageMark) {
        chatMessageService.markChatMessageAsRead(messageMark);
    }
}
