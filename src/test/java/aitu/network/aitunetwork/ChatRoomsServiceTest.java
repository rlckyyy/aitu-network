package aitu.network.aitunetwork;

import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatRoomType;
import aitu.network.aitunetwork.repository.ChatRoomRepository;
import aitu.network.aitunetwork.repository.UserRepository;
import aitu.network.aitunetwork.service.ChatRoomService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

//@Disabled
@DataMongoTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ChatRoomsServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChatRoomService chatRoomService;

    @BeforeAll
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
        User user1 = userRepository.findByIdOrEmail("user1@gmail.com")
                .orElseThrow();
        User user2 = userRepository.findByIdOrEmail("user2@gmail.com")
                .orElseThrow();

        ChatRoomDTO chatRoom = chatRoomService.createChatRoom(
                new NewChatRoomDTO(new HashSet<>(Set.of(user2.getId())), ChatRoomType.ONE_TO_ONE, null),
                user1
        );

        ChatRoomDTO newerChatRoom = chatRoomService.createChatRoom(
                new NewChatRoomDTO(new HashSet<>(Set.of(user1.getId())), ChatRoomType.ONE_TO_ONE, null),
                user2
        );

        assertThat("Another chat room created, but it should not", chatRoom.id(), equalTo(newerChatRoom.id()));
        assertThat("Another chat room created, but it should not", chatRoom.chatId(), equalTo(newerChatRoom.chatId()));
        assertThat("Title of chat room fetching by user1 must be username of user2", chatRoom.title(), equalTo(user2.getUsername()));
        assertThat("Title of chat room fetching by user2 must be username of user1", newerChatRoom.title(), equalTo(user1.getUsername()));
        assertThat("Just created chat room should be empty", chatRoom.empty() && newerChatRoom.empty());
    }

    @AfterAll
    void clearUp() {
        userRepository.deleteAll();
    }
}
