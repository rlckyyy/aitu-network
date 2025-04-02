package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.mapper.ChatMapper;
import aitu.network.aitunetwork.repository.ChatRoomRepository;
import aitu.network.aitunetwork.service.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public Optional<String> getChatId(String sender, String recipient, boolean createIfNotExist) {
        return chatRoomRepository
                .findBySenderAndRecipient(sender, recipient)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }
                    String chatId = ChatUtils.generateChatId(sender, recipient);

                    ChatRoom senderRecipient = ChatMapper.mapToChatRoom(sender, recipient, chatId);
                    ChatRoom recipientSender = ChatMapper.mapToChatRoom(recipient, sender, chatId);

                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }

    public List<ChatRoom> getUserChatRooms(String email) {
        return chatRoomRepository.findAllBySender(email);
    }
}