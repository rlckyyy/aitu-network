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
public class FriendRequest extends BaseEntity {
    private String id;
    private String senderId;
    private String sender;
    private String receiverId;
    private String receiver;
    private FriendRequestStatus status;
}
