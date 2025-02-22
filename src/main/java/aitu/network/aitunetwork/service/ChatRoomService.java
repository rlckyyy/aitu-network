package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.ChatRoom;
import aitu.network.aitunetwork.model.entity.ChatUser;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.repository.ChatRoomRepository;
import aitu.network.aitunetwork.repository.ChatUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatUserRepository chatUserRepository;

    public Optional<String> getChatId(
            String sender, String recipient, boolean createIfNotExist) {

        return chatRoomRepository
                .findBySenderAndRecipient(sender, recipient)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if (!createIfNotExist) {
                        return Optional.empty();
                    }
                    var chatId =
                            String.format("%s_%s", sender, recipient);

                    ChatRoom senderRecipient = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(sender)
                            .recipient(recipient)
                            .build();

                    ChatRoom recipientSender = ChatRoom
                            .builder()
                            .chatId(chatId)
                            .sender(recipient)
                            .recipient(sender)
                            .build();
                    chatRoomRepository.save(senderRecipient);
                    chatRoomRepository.save(recipientSender);

                    return Optional.of(chatId);
                });
    }

    public List<ChatRoom> getUserChatRooms(String email) {
        return chatRoomRepository.findAllBySender(email);
    }

    public List<User> getOnlineUsers() {
        return chatUserRepository.findAllByConnected(true).stream()
                .map(ChatUser::getUser)
                .toList();
    }
}