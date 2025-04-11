package aitu.network.aitunetwork.model.mapper;

import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import aitu.network.aitunetwork.service.util.ChatUtils;

import java.util.List;

import static java.util.Collections.emptyList;

public class ChatMapper {

    /**
     * Maps only new chat room instance
     * */
    public static ChatRoom mapToChatRoom(NewChatRoomDTO dto, List<User> participants) {
        String chatId = ChatUtils.generateChatId(dto);
        return ChatRoom
                .builder()
                .chatId(chatId)
                .title(dto.chatRoomType().equals(ChatRoomType.GROUP) ? dto.title() : null)
                .participants(participants)
                .chatRoomType(dto.chatRoomType())
                .empty(true)
                .build();
    }

    public static ChatRoomDTO mapToChatRoomDTO(ChatRoom chatRoom, User user) {
        return new ChatRoomDTO(
                chatRoom.getId(),
                chatRoom.getChatId(),
                ChatUtils.resolveChatRoomTitle(chatRoom, user),
                UserMapper.toShortDtos(chatRoom.getParticipants() == null ? emptyList() : chatRoom.getParticipants()),
                chatRoom.getChatRoomType(),
                chatRoom.getEmpty()
        );
    }
}
