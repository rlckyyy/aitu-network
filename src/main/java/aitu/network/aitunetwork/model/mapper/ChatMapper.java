package aitu.network.aitunetwork.model.mapper;

import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;

import java.util.List;

import static java.util.Collections.emptyList;

public class ChatMapper {

    /**
     * Maps only new chat room instance.
     * */
    public static ChatRoom mapToNewChatRoom(NewChatRoomDTO dto, List<User> participants) {
        return ChatRoom
                .builder()
                .chatId(dto.chatRoomType().generateChatId(dto.participantsIds()))
                .title(dto.chatRoomType().equals(ChatRoomType.ONE_TO_ONE) ? null : dto.title())
                .participants(participants)
                .chatRoomType(dto.chatRoomType())
                .empty(true)
                .build();
    }

    public static ChatRoomDTO mapToChatRoomDTO(ChatRoom chatRoom, User user) {
        return new ChatRoomDTO(
                chatRoom.getId(),
                chatRoom.getChatId(),
                chatRoom.getChatRoomType().resolveTitle(chatRoom, user),
                UserMapper.toShortDtos(chatRoom.getParticipants() == null ? emptyList() : chatRoom.getParticipants()),
                chatRoom.getChatRoomType(),
                chatRoom.getEmpty()
        );
    }
}
