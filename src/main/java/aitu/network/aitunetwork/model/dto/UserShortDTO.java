package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.entity.Avatar;

public record UserShortDTO(String id, String username, String email, Avatar avatar) {
}
