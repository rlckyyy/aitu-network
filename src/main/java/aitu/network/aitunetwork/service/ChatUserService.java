package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.exception.NotFoundException;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatUser;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatUserService {

    private final MongoTemplate mongoTemplate;
    private final ChatRoomService chatRoomService;
    private final SecureTalkUserRepository secureTalkUserRepository;

    public void connectChatUser(User user) {
        updateUserStatus(user, true, ChatUser.Fields.connectedOn);
    }

    public void disconnectChatUser(User user) {
        updateUserStatus(user, false, ChatUser.Fields.leftOn);
    }

    public void updateUserStatus(User user, boolean connected, String connectedOrLeftOn) {
        Query query = new Query(Criteria.where("_id.username").is(user.getUsername()));
        Update update = new Update()
                .set(ChatUser.Fields.connected, connected)
                .set(connectedOrLeftOn, Instant.now());

        mongoTemplate.updateFirst(query, update, ChatUser.class);
    }

    public List<ChatRoomDTO> getUserChats(String userId) {
        return chatRoomService.getUserChatRooms(fetchUser(userId));
    }

    public List<User> searchUsers(String query, User currentUser) {
        List<User> users = secureTalkUserRepository.findAllByEmailContainsIgnoreCaseOrUsernameContainsIgnoreCase(query, query);
        return users.stream()
                .filter(user -> !user.getEmail().equalsIgnoreCase(currentUser.getEmail()))
                .toList();
    }

    User fetchUser(String userId) {
        return secureTalkUserRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id (" + userId + ") not found"));
    }
}
