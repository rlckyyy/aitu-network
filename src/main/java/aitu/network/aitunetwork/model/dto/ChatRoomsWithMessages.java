package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomsWithMessages {
    private ChatRoomDTO chatRoom;
    private List<ChatMessage> messages;

    public static List<ChatRoomsWithMessages> fromMap(Map<ChatRoomDTO, List<ChatMessage>> map) {
        return map.entrySet().stream()
                .map(entry -> new ChatRoomsWithMessages(entry.getKey(), entry.getValue()))
                .toList();
    }
}