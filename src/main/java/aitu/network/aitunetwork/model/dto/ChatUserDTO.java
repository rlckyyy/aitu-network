package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.enums.MessageType;

public record ChatUserDTO(String username, MessageType type) {
}
