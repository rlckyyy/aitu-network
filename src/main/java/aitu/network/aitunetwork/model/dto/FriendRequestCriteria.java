package aitu.network.aitunetwork.model.dto;

import aitu.network.aitunetwork.model.enums.FriendRequestStatus;

public record FriendRequestCriteria(
        FriendRequestStatus status
) {
}
