package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.dto.chat.MessageMark;
import aitu.network.aitunetwork.model.dto.chat.WSMessage;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.repository.ChatMessageRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final Executor executor;

    @Value("${secure-talk.chat.user-destination}")
    private String destination;

    public ChatMessage save(ChatMessage message) {
        message.setStatus(MessageStatus.DELIVERED);
        chatMessageRepository.save(message);
        return message;
    }

    public long countNewMessages(String chatId, User user) {
        return chatMessageRepository.countByChatIdAndSenderIdIsNotAndStatus(
                chatId, user.getId(), MessageStatus.DELIVERED);
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageRepository.findByChatIdOrderByCreatedAt(chatId);
    }

    public void markChatMessageAsRead(MessageMark messageMark) {
        UpdateResult updateResult = mongoTemplate.updateMulti(
                Query.query(Criteria.where("_id").in(messageMark.getMessageIds())),
                Update.update(ChatMessage.Fields.status, MessageStatus.RECEIVED),
                ChatMessage.class
        );

        long matchedCount = updateResult.getMatchedCount();
        long modifiedCount = updateResult.getModifiedCount();
        if (matchedCount != modifiedCount) {
            throw new RuntimeException(
                    "Matched and modified count are different, matchedCount = " + matchedCount + "; modifiedCount = " + modifiedCount
            );
        }
    }

    public void sendWSMessageToUsers(Collection<String> users, WSMessage message) {
        CompletableFuture.runAsync(() -> users.forEach(user -> sendToUser(user, message)), executor);
    }

    private void sendToUser(String userId, WSMessage WSMessage) {
        messagingTemplate.convertAndSendToUser(
                userId, destination,
                WSMessage
        );
    }
}