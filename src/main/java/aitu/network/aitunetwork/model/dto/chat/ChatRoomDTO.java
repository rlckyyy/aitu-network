package aitu.network.aitunetwork.model.dto.chat;

import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;

import java.util.List;

public record ChatRoomDTO(String id,
                          String chatId,
                          String title,
                          List<UserShortDTO> participants,
                          ChatRoomType chatRoomType,
                          boolean empty) {
}
