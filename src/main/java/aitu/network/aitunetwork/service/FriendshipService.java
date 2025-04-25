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

import java.util.List;
import java.util.Objects;

import static aitu.network.aitunetwork.model.enums.FriendRequestStatus.ACCEPTED;
import static aitu.network.aitunetwork.model.enums.FriendRequestStatus.DECLINED;
import static aitu.network.aitunetwork.model.enums.FriendRequestStatus.PENDING;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendshipService {
    private final FriendRequestRepository friendRequestRepository;
    private final UserService userService;

    public List<FriendRequest> getRequests(FriendRequestStatus status, User user) {
        return switch (status) {
            case PENDING -> friendRequestRepository.findByReceiverIdAndStatus(user.getId(), PENDING);
            case ACCEPTED -> friendRequestRepository.findByReceiverIdAndStatus(user.getId(), ACCEPTED);
            case DECLINED -> friendRequestRepository.findByReceiverIdAndStatus(user.getId(), DECLINED);
            case null -> friendRequestRepository.findByReceiverId(user.getId());
        };
    }

    public FriendRequest sendFriendRequest(String id, User user) {
        getSentRequests(PENDING, user).stream()
                .filter(r -> Objects.equals(r.getReceiverId(), id))
                .findAny()
                .ifPresent(r -> {
                    throw new ConflictException("You already have pending friendship request with this user");
                });

        var receiver = userService.getById(id);
        var req = FriendRequest.builder()
                .receiverId(id)
                .receiver(receiver.getUsername())
                .senderId(user.getId())
                .sender(user.getUsername())
                .status(PENDING).build();
        return friendRequestRepository.save(req);
    }

    public List<FriendRequest> getSentRequests(FriendRequestStatus status, User user) {
        return switch (status) {
            case PENDING -> friendRequestRepository.findBySenderIdAndStatus(user.getId(), PENDING);
            case ACCEPTED -> friendRequestRepository.findBySenderIdAndStatus(user.getId(), ACCEPTED);
            case DECLINED -> friendRequestRepository.findBySenderIdAndStatus(user.getId(), DECLINED);
            case null -> friendRequestRepository.findBySenderId(user.getId());
        };
    }

    public void deleteRequest(String requestId, User currentUser) {
        var friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(FriendRequest.class, requestId));
        if (!currentUser.getId().equals(friendRequest.getSenderId())) {
            throw new ConflictException("User is not owner of the request");
        }
        friendRequestRepository.delete(friendRequest);
    }


    public FriendRequest respondRequest(String requestId, FriendRequestStatus status, User receiver) {
        FriendRequest request = findFriendRequestById(requestId);

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
        if (receiver.getFriendList().contains(sender.getId())) {
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
        userService.save(List.of(receiver, sender));
    }

    private FriendRequest saveRequest(FriendRequest request) {
        return friendRequestRepository.save(request);
    }

    public List<User> getUserFriendList(String userId) {
        return userService.getUserFriends(userId);
    }
}
