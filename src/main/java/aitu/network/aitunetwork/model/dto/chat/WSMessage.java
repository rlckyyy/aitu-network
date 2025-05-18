package aitu.network.aitunetwork.model.dto.chat;

import aitu.network.aitunetwork.model.dto.WSMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WSMessage {
    private WSMessageType type;
    private Object message;
}
