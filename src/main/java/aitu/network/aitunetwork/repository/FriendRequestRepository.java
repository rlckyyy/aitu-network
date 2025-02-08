package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.FriendRequest;
import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findByReceiverIdAndStatus(String id, FriendRequestStatus status);

    List<FriendRequest> findByReceiverId(String id);

    List<FriendRequest> findBySenderId(String id);

    List<FriendRequest> findBySenderIdAndStatus(String id, FriendRequestStatus status);
}
