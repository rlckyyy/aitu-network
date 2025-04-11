package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.mapper.UserMapper;
import aitu.network.aitunetwork.repository.SecureTalkUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {
    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final ChatUserService chatUserService;
    private final SecureTalkUserRepository userRepository;

    public void processMessage(
            ChatMessage newChatMessage
    ) {
        ChatMessage chatMessage = chatMessageService.save(newChatMessage);
        ChatRoom chatRoom = chatRoomService.getChatRoom(chatMessage.getChatId());
        chatRoom = chatRoomService.markChatRoom(chatRoom, false);
        List<User> participants = chatRoom.getParticipants().stream()
                .filter(participant -> !participant.getId().equals(chatMessage.getSenderId()))
                .toList();
        System.out.println(participants);

        chatRoom.getParticipants().stream()
                .filter(participant -> !participant.getId().equals(chatMessage.getSenderId()))
                .forEach(participant -> messagingTemplate.convertAndSendToUser(participant.getId(), "/queue/messages", chatMessage));
    }

    public Map<String, Long> countNewMessages(
            String chatId
    ) {
        long count = chatMessageService.countNewMessages(chatId);
        return Map.of("count", count);
    }

    public List<ChatMessage> findChatMessages(String chatId) {
        return chatMessageService.findChatMessages(chatId);
    }

    public List<ChatRoomDTO> getUserChatRooms(String id) {
        return chatUserService.getUserChats(id);
    }

    public List<User> searchUsers(String query, User user) {
        return chatUserService.searchUsers(query, user);
    }

    public ChatRoomDTO createChatRoom(NewChatRoomDTO chatRoom, User user) {
        return chatRoomService.createChatRoom(chatRoom, user);
    }

    public Collection<UserShortDTO> getRelatedUsers(User user) {
        Map<String, UserShortDTO> contactedUsersMap = chatRoomService.getUserChatRooms(user).stream()
                .map(ChatRoomDTO::participants)
                .flatMap(List::stream)
                .filter(participant -> !participant.id().equals(user.getId()))
                .collect(Collectors.toMap(UserShortDTO::id, Function.identity(), (e, r) -> e));

        Collection<UserShortDTO> contactedUsers = contactedUsersMap.values();
        Set<String> contactedUserIds = contactedUsersMap.keySet();
        List<String> nonContactedFriendsIds = user.getFriendList().stream()
                .filter(userId -> !contactedUserIds.contains(userId))
                .toList();

        return userRepository.findAllById(nonContactedFriendsIds).stream()
                .map(UserMapper::toShortDto)
                .collect(
                        () -> new LinkedHashSet<>(contactedUsers),
                        HashSet::add,
                        AbstractCollection::addAll
                );
    }
}
