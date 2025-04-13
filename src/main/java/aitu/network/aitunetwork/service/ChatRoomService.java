package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.exception.NotFoundException;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import aitu.network.aitunetwork.model.mapper.ChatMapper;
import aitu.network.aitunetwork.repository.ChatRoomRepository;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static aitu.network.aitunetwork.model.mapper.ChatMapper.mapToChatRoomDTO;
import static aitu.network.aitunetwork.service.util.ChatUtils.generateTwoPossibleChatIds;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final SecureTalkUserRepository userRepository;

    public List<ChatRoomDTO> getUserChatRooms(User user) {
        return chatRoomRepository.findAllByParticipantsContains(user).stream()
                .filter(chatRoom -> {
                    if (chatRoom.getChatRoomType().equals(ChatRoomType.ONE_TO_ONE)) {
                        return !chatRoom.getEmpty();
                    } else {
                        return true;
                    }
                })
                .map((ChatRoom chatRoom1) -> ChatMapper.mapToChatRoomDTO(chatRoom1, user))
                .collect(Collectors.toList());
    }

    public ChatRoomDTO createChatRoom(NewChatRoomDTO dto, User user) {
        if (dto.chatRoomType().equals(ChatRoomType.ONE_TO_ONE)) {
            List<String> chatIds = generateTwoPossibleChatIds(new ArrayList<>(dto.participantsIds()));
            Optional<ChatRoom> maybeChatRoom = chatRoomRepository.findByChatIdIn(chatIds);
            if (maybeChatRoom.isPresent()) {
                return mapToChatRoomDTO(maybeChatRoom.get(), user);
            }
        }
        List<User> participants = new ArrayList<>(userRepository.findAllById(dto.participantsIds()));
        if (!dto.participantsIds().contains(user.getId())) {
            participants.add(user);
        }
        ChatRoom chatRoom = chatRoomRepository.save(ChatMapper.mapToChatRoom(dto, participants));
        return mapToChatRoomDTO(chatRoom, user);
    }

    public ChatRoom getChatRoom(String chatId) {
        return chatRoomRepository.findByChatIdIn(Collections.singletonList(chatId))
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
                .orElseThrow(() -> new NotFoundException("User room with id: " + id + " not found"));
    }

    public ChatRoom fetch(String id) {
        return chatRoomRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Chat room with id: " + id + " not found"));
    }
}