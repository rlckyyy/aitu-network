package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.model.entity.FriendRequest;
import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import aitu.network.aitunetwork.repository.FriendRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static aitu.network.aitunetwork.model.enums.FriendRequestStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    public List<FriendRequest> getRequests(FriendRequestStatus status) {
        var user = userService.getCurrentUser();
        List<FriendRequest> friendRequests;
        switch (status) {
            case PENDING -> friendRequests = friendRequestRepository.findByReceiverIdAndStatus(user.getId(), PENDING);
            case ACCEPTED -> friendRequests = friendRequestRepository.findByReceiverIdAndStatus(user.getId(), ACCEPTED);
            case DECLINED -> friendRequests = friendRequestRepository.findByReceiverIdAndStatus(user.getId(), DECLINED);
            case null -> friendRequests = friendRequestRepository.findByReceiverId(user.getId());
            default -> {
                friendRequests = new ArrayList<>();
                log.info("default case receive req");
            }
        }
        return friendRequests;
    }

    public FriendRequest sendFriendRequest(String id) {
        var user = userService.getCurrentUser();
        var req = FriendRequest.builder()
                .receiverId(id)
                .senderId(user.getId())
                .status(PENDING).build();
        return friendRequestRepository.save(req);
    }

    public List<FriendRequest> getSentRequests(FriendRequestStatus status) {
        var user = userService.getCurrentUser();
        List<FriendRequest> friendRequests;
        switch (status) {
            case PENDING -> friendRequests = friendRequestRepository.findBySenderIdAndStatus(user.getId(), PENDING);
            case ACCEPTED -> friendRequests = friendRequestRepository.findBySenderIdAndStatus(user.getId(), ACCEPTED);
            case DECLINED -> friendRequests = friendRequestRepository.findBySenderIdAndStatus(user.getId(), DECLINED);
            case null -> friendRequests = friendRequestRepository.findBySenderId(user.getId());
            default -> {
                friendRequests = new ArrayList<>();
                log.info("default case sent req");
            }
        }
        return friendRequests;
    }

    public void deleteRequest(String requestId) {
        var currentUser = userService.getCurrentUser();
        var friendRequest = friendRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(FriendRequest.class, requestId));
        if (!currentUser.getId().equals(friendRequest.getSenderId())) {
            throw new ConflictException("User is not owner of the request");
        }
        friendRequestRepository.delete(friendRequest);
    }

    // person will get info about his requests [id1,id2,id3] he should respond
    // now write
    public FriendRequest respondRequest(String requestId, FriendRequestStatus status) {
        var user = userService.getCurrentUser();
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(FriendRequest.class, requestId));
        if (!request.getReceiverId().equals(user.getId())) {
            throw new ConflictException("User is not for this user");
        }
        if (PENDING.equals(status)) {
            throw new ConflictException("request respond can not be PENDING");
        }
        request.setStatus(status);
        return friendRequestRepository.save(request);
    }
}
