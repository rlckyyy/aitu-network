package aitu.network.aitunetwork.model.dto.chat;

import lombok.Data;

@Data
public class IceCandidate {
    private String candidate;
    private String sdpMid;
    private Integer sdpMLineIndex;
}