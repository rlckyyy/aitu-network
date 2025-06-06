package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {

    long countByChatIdAndSenderIdIsNotAndStatus(String chatId, String participantId, MessageStatus status);

    List<ChatMessage> findByChatIdOrderByCreatedAt(String chatId);
}