package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final MongoOperations mongoOperations;

    public ChatMessage save(ChatMessage message) {
        message.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(message);
        return message;
    }

    public long countNewMessages(String chatId) {
        return chatMessageRepository.countByChatIdAndStatus(
                chatId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageRepository.findByChatIdOrderByCreatedAt(chatId);
    }
}