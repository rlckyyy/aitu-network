package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.enums.MessageStatus;
import aitu.network.aitunetwork.model.enums.MessageType;
import aitu.network.aitunetwork.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;
    private final FileService fileService;

    public ChatMessage save(ChatMessage message) {
        message.setStatus(MessageStatus.RECEIVED);
        chatMessageRepository.save(message);
        return message;
    }

    public ChatMessage saveAudioMessage(ChatMessage chatMessage, MultipartFile audioFile) {
        if (!chatMessage.getType().equals(MessageType.MESSAGE_AUDIO)) {
            throw new IllegalArgumentException("Message type is text");
        }
        String fileId = fileService.uploadFile(audioFile);
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
}