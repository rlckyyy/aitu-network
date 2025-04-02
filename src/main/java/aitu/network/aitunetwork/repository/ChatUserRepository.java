package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.chat.ChatUser;
import aitu.network.aitunetwork.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatUserRepository extends MongoRepository<ChatUser, User> {
}
