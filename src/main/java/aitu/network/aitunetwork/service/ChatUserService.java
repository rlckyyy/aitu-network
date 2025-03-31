package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.ChatRoom;
import aitu.network.aitunetwork.model.entity.ChatUser;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.ChatUserRepository;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatUserService {

    private final MongoTemplate mongoTemplate;
    private final ChatUserRepository chatUserRepository;
    private final ChatRoomService chatRoomService;
    private final SecureTalkUserRepository secureTalkUserRepository;
    private final UserService userService;

    public void saveChatUser(User user) {
        ChatUser chatUser = mapToChatUser(user);
        chatUserRepository.save(chatUser);
    }

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

    private ChatUser mapToChatUser(User user) {
        return new ChatUser(user, null, null, null, null);
    }

    public List<ChatRoom> getUserChats(String email) {
        return chatRoomService.getUserChatRooms(email);
    }

    public List<User> searchUsers(String query) {
        List<User> users = secureTalkUserRepository.findAllByEmailContainsIgnoreCase(query);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.isAuthenticated()) {
            return users;
        }

        String currentUserEmail = userService.getCurrentUser().getEmail();
        return users.stream()
                .filter(user -> !user.getEmail().equalsIgnoreCase(currentUserEmail))
                .toList();
    }
}
