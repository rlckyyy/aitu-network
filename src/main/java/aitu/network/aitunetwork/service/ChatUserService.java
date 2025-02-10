package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.ChatRoom;
import aitu.network.aitunetwork.model.entity.ChatUser;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatUserService {

    private final MongoTemplate mongoTemplate;
    private final ChatUserRepository chatUserRepository;
    private final ChatRoomService chatRoomService;

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

    public List<ChatRoom> getUserChats(String username) {
        return chatRoomService.getUserChatRooms(username);
    }
}
