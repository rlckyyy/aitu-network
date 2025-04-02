package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderAndRecipient(String sender, String recipient);
    List<ChatRoom> findAllBySender(String sender);
}