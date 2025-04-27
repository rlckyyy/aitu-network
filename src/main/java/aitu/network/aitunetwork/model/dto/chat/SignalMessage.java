package aitu.network.aitunetwork.model.dto.chat;

import lombok.Data;

@Data
public class SignalMessage {
    private String type;
    private String senderId;
    private String companionId;
    private Object sdp;
    private Object candidate;
}