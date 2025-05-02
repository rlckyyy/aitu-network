package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.ChatRoomsWithMessages;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms/{id}/messages/count")
    public ResponseEntity<Map<String, Object>> countNewMessages(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(chatService.countNewMessages(id));
    }

    @GetMapping("/rooms/{chatId}/messages")
    public List<ChatMessage> findChatMessages(@PathVariable String chatId) {
        return chatService.findChatMessages(chatId);
    }

    @GetMapping("/{userId}")
    public List<ChatRoomsWithMessages> findChatMessages(
            @PathVariable String userId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return chatService.findChats(userId, userDetails.user());
    }

    @GetMapping("/rooms/{userId}")
    public List<ChatRoomDTO> findUserChatRooms(
            @PathVariable(required = false) String userId,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return chatService.findUserChatRooms(userId, userDetails.user());
    }

    @GetMapping("/users/search")
    public List<User> searchUsers(@RequestParam String query, @CurrentUser CustomUserDetails currentUser) {
        return chatService.searchUsers(query, currentUser.user());
    }

    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomDTO createChatRoom(@Valid @RequestBody NewChatRoomDTO chatRoom,
                                      @CurrentUser CustomUserDetails userDetails) {
        return chatService.createChatRoom(chatRoom, userDetails.user());
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/rooms/{id}/participants/{participantId}")
    public void addUserToChatRoom(@PathVariable String id, @PathVariable String participantId) {
        chatService.addParticipantToChatRoom(id, participantId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/rooms/{id}/participants/{participantId}")
    public void deleteUserFromChatRoom(@PathVariable String id, @PathVariable String participantId) {
        chatService.deleteUserFromChatRoom(id, participantId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages/files")
    public ChatMessage saveMessageFile(
            @Valid @RequestPart("chatMessage") ChatMessage chatMessage,
            @RequestPart("file") MultipartFile audioFile
    ) {
        return chatService.saveFileMessage(chatMessage, audioFile);
    }
}