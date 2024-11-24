package aitu.network.aitunetwork.service.impl;


import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.FriendRequest;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.FriendRequestStatus;
import aitu.network.aitunetwork.repository.FriendRequestRepository;
import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public User save(UserDTO userDTO) {
        return userRepository.save(User.builder()
                .username(userDTO.username())
                .password(userDTO.password())
                .email(userDTO.email())
                .build());
    }

    public void sendFriendRequest(Long senderId, Long receiverId) {
        if (senderId.equals(receiverId)) {
            throw new ConflictException("senderId and receiverId are the same");
        }

        User sender = userRepository.findById(senderId).orElseThrow(() -> new EntityNotFoundException(User.class, senderId));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new EntityNotFoundException(User.class, receiverId));

        FriendRequest friendRequest = FriendRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(friendRequest);
    }

    public void acceptFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(FriendRequest.class, requestId));

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }
        request.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(request);

        User sender = request.getSender();
        User receiver = request.getReceiver();

        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    public void declineFriendRequest(Long requestId) {
        FriendRequest request = friendRequestRepository.findById(requestId).orElseThrow(() -> new EntityNotFoundException(FriendRequest.class, requestId));

        if (request.getStatus() != FriendRequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }

        request.setStatus(FriendRequestStatus.PENDING);
        friendRequestRepository.save(request);
    }


}
