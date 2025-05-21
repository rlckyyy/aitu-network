package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.enums.AccessType;

public record UserUpdateDTO(
        String username,
        String description,
        AccessType accessType
) {
}
