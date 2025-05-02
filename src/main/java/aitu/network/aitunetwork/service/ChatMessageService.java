package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final FileService fileService;

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

    public long countNewMessages(String chatId) {
        return chatMessageRepository.countByChatIdAndStatus(
                chatId, MessageStatus.DELIVERED);
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageRepository.findByChatIdOrderByCreatedAt(chatId);
    }

    public List<ChatMessage> findChatMessages(Collection<String> chatIds) {
        return chatMessageRepository.findAllByChatIdIn(chatIds);
    }
}