package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.repository.ChatMessageRepository;
import com.mongodb.client.result.UpdateResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final FileService fileService;
    private final MongoTemplate mongoTemplate;

    public ChatMessage save(ChatMessage message) {
        message.setStatus(MessageStatus.DELIVERED);
        chatMessageRepository.save(message);
        return message;
    }

    public ChatMessage saveFileMessage(ChatMessage chatMessage, MultipartFile file) {
        if (!chatMessage.getType().isFileMessageType) {
            throw new IllegalArgumentException("Message type is non file type");
        }
        String fileId = fileService.uploadFile(file);
        String link = fileService.getLinkForResource(fileId);
        chatMessage.setContent(link);
        save(chatMessage);
        return chatMessage;
    }

    public long countNewMessages(String chatId, User user) {
        return chatMessageRepository.countByChatIdAndSenderIdIsNotAndStatus(
                chatId, user.getId(), MessageStatus.DELIVERED);
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageRepository.findByChatIdOrderByCreatedAt(chatId);
    }

    public List<ChatMessage> findChatMessages(Collection<String> chatIds) {
        return chatMessageRepository.findAllByChatIdIn(chatIds);
    }

    public void findChatMessage(String messageId) {
        UpdateResult updateResult = mongoTemplate.updateFirst(
                Query.query(Criteria.where("_id").is(messageId)),
                Update.update(ChatMessage.Fields.status, MessageStatus.RECEIVED),
                ChatMessage.class
        );
        long matchedCount = updateResult.getMatchedCount();
        long modifiedCount = updateResult.getModifiedCount();
        if (matchedCount != modifiedCount) {
            throw new RuntimeException(
                    "Matched and modified count are different, matchedCount=" + matchedCount + "; modifiedCount=" + modifiedCount
            );
        }
    }
}