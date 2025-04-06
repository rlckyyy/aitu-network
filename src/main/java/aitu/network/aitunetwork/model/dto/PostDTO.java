package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.enums.PostType;

public record PostDTO(
        String ownerId,
        String groupId,
        PostType postType,
        String description
) {
}
