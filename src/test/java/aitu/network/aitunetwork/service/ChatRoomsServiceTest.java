package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoom;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import aitu.network.aitunetwork.repository.ChatRoomRepository;
import aitu.network.aitunetwork.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@DataMongoTest
public class ChatRoomsServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @BeforeEach
    void prepareUsers() {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .username("user1")
                .build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .username("user2")
                .build();
        userRepository.saveAll(List.of(user1, user2));
    }

    @TestConfiguration
    static class Config {
        @Bean
        ChatRoomService chatService(
                ChatRoomRepository chatRoomRepository,
                UserRepository userRepository
        ) {
            return new ChatRoomService(chatRoomRepository, userRepository);
        }
    }

    @Test
    void createChatRoom_shouldReturnAlreadyCreatedChatRoom_anotherUserCreatedChatRoom() {
        User user1 = getUserByEmail("user1@gmail.com");
        User user2 = getUserByEmail("user2@gmail.com");

        ChatRoomDTO chatRoom = chatRoomService.createChatRoom( // user1 is creator
                new NewChatRoomDTO(new HashSet<>(Set.of(user2.getId())), ChatRoomType.ONE_TO_ONE, null),
                user1
        );

        ChatRoomDTO newerChatRoom = chatRoomService.createChatRoom( // user2 is creator
                new NewChatRoomDTO(new HashSet<>(Set.of(user1.getId())), ChatRoomType.ONE_TO_ONE, null),
                user2
        );

        assertThat("Another chat room created, but it should not", chatRoom.id(), equalTo(newerChatRoom.id()));
        assertThat("Another chat room created, but it should not", chatRoom.chatId(), equalTo(newerChatRoom.chatId()));
        assertThat("Title of chat room fetching by user1 must be username of user2", chatRoom.title(), equalTo(user2.getUsername()));
        assertThat("Title of chat room fetching by user2 must be username of user1", newerChatRoom.title(), equalTo(user1.getUsername()));
        assertThat("Just created chat room should be empty", chatRoom.empty() && newerChatRoom.empty());
    }

    @Test
    void createChatRoom_shouldReturnChatRoomWithTitleThatRelatedToCurrentUser_whenTitleIsPresentInRequestDto() {
        User user1 = getUserByEmail("user1@gmail.com");
        User user2 = getUserByEmail("user2@gmail.com");

        ChatRoomDTO chatRoom = chatRoomService.createChatRoom( // user1 is creator
                new NewChatRoomDTO(new HashSet<>(Set.of(user2.getId())), ChatRoomType.ONE_TO_ONE, "chat room title"),
                user1
        );

        // imagine that creator wrote something in the chat room (because it is chat message service's responsibility)
        {
            ChatRoom justCreatedChatRoom = chatRoomRepository.findById(chatRoom.id()).orElseThrow();
            justCreatedChatRoom.setEmpty(false);
            chatRoomRepository.save(justCreatedChatRoom);
        }

        List<ChatRoomDTO> user1ChatRooms = chatRoomService.getUserChatRooms(user1);

        Optional<ChatRoomDTO> chatRoomOptional = user1ChatRooms.stream()
                .filter(dto -> dto.id().equals(chatRoom.id()))
                .findFirst();

        assertThat(chatRoomOptional.isPresent(), equalTo(true));

        ChatRoomDTO chatRoomDTO = chatRoomOptional.get();

        assertThat("one to one chat room must have title, that is username of other user", chatRoomDTO.title(), notNullValue());
        assertThat("", chatRoomDTO.title(), equalTo("user2"));
    }

    @Test
    @DisplayName("""
            if user1 created one to one chat room with user2,
            user2 should not see chat room that was created by user1 until user1 writes something (chat room is NOT empty)
            """)
    void markChatRoom_shouldNotReturnCreatedChatRoomOfAnother_whenChatRoomIsEmpty() {
        User user1 = getUserByEmail("user1@gmail.com");
        User user2 = getUserByEmail("user2@gmail.com");

        ChatRoomDTO chatRoom = chatRoomService.createChatRoom( // user1 is creator
                new NewChatRoomDTO(new HashSet<>(Set.of(user2.getId())), ChatRoomType.ONE_TO_ONE, null),
                user1
        );

        List<ChatRoomDTO> user2ChatRooms = chatRoomService.getUserChatRooms(user2);

        assertThat(
                "user2 should not see that another user created one to one chat room with him",
                user2ChatRooms.stream()
                        .noneMatch(chatRoomDTO -> chatRoomDTO.id().equals(chatRoom.id()))
        );
    }

    @Test
    @DisplayName("""
            if user1 created one to one chat room with user2,
            user1 should not see chat room that was created by himself after fetching chat rooms again
            """)
    void markChatRoom_shouldNotReturnCreatedChatRoomOfHimself_whenChatRoomIsEmpty() {
        User user1 = getUserByEmail("user1@gmail.com");
        User user2 = getUserByEmail("user2@gmail.com");

        ChatRoomDTO chatRoom = chatRoomService.createChatRoom( // user1 is creator
                new NewChatRoomDTO(new HashSet<>(Set.of(user2.getId())), ChatRoomType.ONE_TO_ONE, null),
                user1
        );

        List<ChatRoomDTO> user1ChatRooms = chatRoomService.getUserChatRooms(user1);

        assertThat(
                "user2 should not see that another user created one to one chat room with him",
                user1ChatRooms.stream()
                        .noneMatch(chatRoomDTO -> chatRoomDTO.id().equals(chatRoom.id()))
        );
    }

    @AfterEach
    void clearUp() {
        userRepository.deleteAll();
        chatRoomRepository.deleteAll();
    }

    private User getUserByEmail(String email) {
        return userRepository.findByIdOrEmail(email)
                .orElseThrow();
    }
}
