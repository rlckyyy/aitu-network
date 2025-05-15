package aitu.network.aitunetwork.model.dto.chat;

import lombok.Data;

import java.util.Collection;

@Data
public class MessageMark {
    private Collection<String> messageIds;
}