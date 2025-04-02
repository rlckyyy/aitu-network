package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.exception.NotFoundException;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final MongoOperations mongoOperations;

    public ChatMessage save(ChatMessage message) {
        message.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(message);
        return message;
    }

    public long countNewMessages(String senderId, String recipientId) {
        return chatMessageRepository.countBySenderAndRecipientAndStatus(
                senderId, recipientId, MessageStatus.RECEIVED);
    }

    public List<ChatMessage> findChatMessages(String sender, String recipient) {
        var messages = chatRoomService.getChatId(sender, recipient, false)
                .map(chatMessageRepository::findByChatId)
                .orElse(new ArrayList<>());

        if (!messages.isEmpty()) {
            updateStatuses(sender, recipient, MessageStatus.DELIVERED);
        }

        return messages;
    }

    public ChatMessage findById(String id) {
//                            chatMessage.setStatus(MessageStatus.DELIVERED);
        return chatMessageRepository
                .findById(id)
                .map(chatMessageRepository::save)
                .orElseThrow(() ->
                        new NotFoundException("can't find message (" + id + ")"));
    }

    public void updateStatuses(String sender, String recipient, MessageStatus status) {
        Query query = new Query(
                Criteria
                        .where("sender").is(sender)
                        .and("recipient").is(recipient));
        Update update = Update.update("status", status);
        mongoOperations.updateMulti(query, update, ChatMessage.class);
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageRepository.findByChatId(chatId);
    }
}