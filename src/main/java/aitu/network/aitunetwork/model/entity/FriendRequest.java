package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FriendRequest {
    private String id;
    private String senderId;
    private String receiverId;
    private FriendRequestStatus status;
}
