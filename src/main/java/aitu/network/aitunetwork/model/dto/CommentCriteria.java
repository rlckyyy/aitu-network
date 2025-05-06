package aitu.network.aitunetwork.model.dto;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public record CommentCriteria(
        String postId,
        String userId,
        String groupId
) {
}
