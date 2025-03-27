package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.enums.AccessType;

public record GroupCreateDTO(
        String name,
        String description,
        AccessType accessType) {
}
