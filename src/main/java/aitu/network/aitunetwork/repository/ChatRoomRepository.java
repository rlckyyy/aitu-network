package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    List<ChatRoom> findAllByParticipantsContains(User participant);
    Optional<ChatRoom> findByChatId(String chatId);
}