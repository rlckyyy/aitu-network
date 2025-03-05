package aitu.network.aitunetwork.model.mapper;

import aitu.network.aitunetwork.model.entity.ChatRoom;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ChatMapper {

    public ChatRoom mapToChatRoom(String sender, String recipient, String chatId) {
        return ChatRoom
                .builder()
                .chatId(chatId)
                .sender(sender)
                .recipient(recipient)
                .build();
    }
}
