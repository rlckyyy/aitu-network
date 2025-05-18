package aitu.network.aitunetwork.model.dto.chat;

import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
public class ChatRoomsWithMessages {
    private ChatRoomDTO chatRoom;
    private List<ChatMessage> messages;
}