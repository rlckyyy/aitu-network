package aitu.network.aitunetwork.service;


import aitu.network.aitunetwork.model.dto.UserDTO;
import aitu.network.aitunetwork.model.entity.User;

public interface UserService {
    User save(UserDTO user);
    void sendFriendRequest(Long senderId, Long receiverId);
    void acceptFriendRequest(Long requestId);
    void declineFriendRequest(Long requestId);
}
