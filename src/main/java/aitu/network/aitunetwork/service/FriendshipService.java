package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.model.entity.FriendRequest;
import aitu.network.aitunetwork.model.entity.User;
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
        if (id == null || id.isBlank()) {
            throw new ConflictException("user id is null");
        }
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
        if (requestId == null || requestId.isBlank()) {
            throw new ConflictException("requestId is null");
        }
        var currentUser = userService.getCurrentUser();
        var friendRequest = friendRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(FriendRequest.class, requestId));
        if (!currentUser.getId().equals(friendRequest.getSenderId())) {
            throw new ConflictException("User is not owner of the request");
        }
        friendRequestRepository.delete(friendRequest);
    }


    public FriendRequest respondRequest(String requestId, FriendRequestStatus status) {
        FriendRequest request = findFriendRequestById(requestId);

        var receiver = userService.getCurrentUser();
        var sender = userService.getById(request.getSenderId());

        checkIfAlreadyFriends(receiver, sender);

        validateRequestForOwner(receiver, sender, status);

        if (ACCEPTED.equals(status)) {
            acceptFriendRequest(receiver, sender);
        }
        request.setStatus(status);
        return saveRequest(request);
    }

    private FriendRequest findFriendRequestById(String requestId) {
        return friendRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(FriendRequest.class, requestId));
    }

    private void checkIfAlreadyFriends(User receiver, User sender) {
        boolean alreadyAreFriends = receiver.getFriendList()
                .stream()
                .anyMatch(r -> r.equals(sender.getEmail()));
        if (alreadyAreFriends) {
            throw new ConflictException("Users are already friends");
        }
    }

    private void validateRequestForOwner(User receiver, User sender, FriendRequestStatus status) {
        if (receiver.getId().equals(sender.getId())) {
            throw new ConflictException("Friend request is not for the owner user");
        }
        if (PENDING.equals(status)) {
            throw new ConflictException("Request response cannot be PENDING");
        }
    }

    private void acceptFriendRequest(User receiver, User sender) {
        receiver.addFriendList(sender);
        userService.save(receiver);
        userService.save(sender);
    }

    private FriendRequest saveRequest(FriendRequest request) {
        return friendRequestRepository.save(request);
    }
}
