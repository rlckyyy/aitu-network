package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend_requests")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private FriendRequestStatus status;

}
