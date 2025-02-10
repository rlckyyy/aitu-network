package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository
        extends MongoRepository<ChatMessage, String> {

    long countBySenderAndRecipientAndStatus(
            String sender, String recipient, MessageStatus status);

    List<ChatMessage> findByChatId(String chatId);
}