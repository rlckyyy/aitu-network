package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.exception.NotFoundException;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.mapper.ChatMapper;
import aitu.network.aitunetwork.repository.ChatRoomRepository;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final SecureTalkUserRepository userRepository;

    public List<ChatRoomDTO> getUserChatRooms(User user) {
        return chatRoomRepository.findAllByParticipantsContains(user).stream()
                .filter(chatRoom -> chatRoom.getChatRoomType().isVisibleForParticipants(chatRoom))
                .map((ChatRoom chatRoom) -> ChatMapper.mapToChatRoomDTO(chatRoom, user))
                .collect(Collectors.toList());
    }

    public ChatRoomDTO createChatRoom(NewChatRoomDTO dto, User user) {
        try {
            Set<User> participants = new HashSet<>(userRepository.findAllById(dto.participantsIds()));
            if (!dto.participantsIds().contains(user.getId())) {
                participants.add(user);
            }
            ChatRoom chatRoom = chatRoomRepository.save(ChatMapper.mapToNewChatRoom(dto, new ArrayList<>(participants)));
            return ChatMapper.mapToChatRoomDTO(chatRoom, user);
        } catch (DuplicateKeyException e) {
            String chatId = dto.chatRoomType().generateChatId(dto.participantsIds());
            return chatRoomRepository.findByChatId(chatId)
                    .map(chatRoom -> ChatMapper.mapToChatRoomDTO(chatRoom, user))
                    .orElseThrow(() -> new NotFoundException("Chat room with chat id: " + chatId + "not found"));
        }
    }

    public ChatRoom getChatRoom(String chatId) {
        return chatRoomRepository.findByChatId(chatId)
                .orElseThrow(() -> new NotFoundException("Chat room with chat id: " + chatId + " not found"));
    }

    public ChatRoom markChatRoom(ChatRoom chatRoom, boolean empty) {
        chatRoom.setEmpty(empty);
        return chatRoomRepository.save(chatRoom);
    }

    public void actionParticipantToChatRoom(String chatRoomId, String participantId, BiFunction<ChatRoom, User, Boolean> action) {
        User participant = fetchUser(participantId);
        ChatRoom chatRoom = fetch(chatRoomId);

        action.apply(chatRoom, participant);
        chatRoomRepository.save(chatRoom);
    }

    public User fetchUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User with id: " + id + " not found"));
    }

    public ChatRoom fetch(String id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat room with id: " + id + " not found"));
    }
}