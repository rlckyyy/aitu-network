package aitu.network.aitunetwork.model.dto.chat;

import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;

import java.util.List;
import java.util.Objects;

public record ChatRoomDTO(String id,
                          String chatId,
                          String title,
                          List<UserShortDTO> participants,
                          ChatRoomType chatRoomType,
                          boolean empty) {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomDTO that = (ChatRoomDTO) o;
        return empty == that.empty && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(chatId, that.chatId) && chatRoomType == that.chatRoomType && Objects.equals(participants, that.participants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chatId, title, participants, chatRoomType, empty);
    }
}
