package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.common.annotations.CurrentUser;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.UserShortDTO;
import aitu.network.aitunetwork.model.dto.chat.ChatRoomDTO;
import aitu.network.aitunetwork.model.dto.chat.NewChatRoomDTO;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.service.ChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @ResponseBody
    @GetMapping("/rooms/{id}/messages/count")
    public ResponseEntity<Map<String, Object>> countNewMessages(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(chatService.countNewMessages(id));
    }

    @ResponseBody
    @GetMapping("/rooms/{chatId}/messages")
    public List<ChatMessage> findChatMessages(@PathVariable String chatId) {
        return chatService.findChatMessages(chatId);
    }

    @ResponseBody
    @GetMapping("/rooms/{userId}")
    public List<ChatRoomDTO> getUserChatRooms(@NotBlank @PathVariable String userId, @CurrentUser CustomUserDetails userDetails) {
        return chatService.getUserChatRooms(userId, userDetails.user());
    }

    @ResponseBody
    @GetMapping("/users/search")
    public List<User> searchUsers(@RequestParam String query, @CurrentUser CustomUserDetails currentUser) {
        return chatService.searchUsers(query, currentUser.user());
    }

    @ResponseBody
    @PostMapping("/rooms")
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomDTO createChatRoom(@Valid @RequestBody NewChatRoomDTO chatRoom,
                                      @CurrentUser CustomUserDetails userDetails) {
        return chatService.createChatRoom(chatRoom, userDetails.user());
    }

    @ResponseBody
    @GetMapping("/users/related")
    public Collection<UserShortDTO> getRelatedUsers(@CurrentUser CustomUserDetails user) {
        return chatService.getRelatedUsers(user.user());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/rooms/{id}/{participantId}")
    public void addUserToChatRoom(@PathVariable String id, @PathVariable String participantId) {
        chatService.addParticipantToChatRoom(id, participantId);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/rooms/{id}/{participantId}")
    public void deleteUserFromChatRoom(@PathVariable String id, @PathVariable String participantId) {
        chatService.deleteUserFromChatRoom(id, participantId);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages/files")
    public ChatMessage saveMessageFile(
            @Valid @RequestPart("chatMessage") ChatMessage chatMessage,
            @RequestPart("file") MultipartFile audioFile
    ) {
        return chatService.saveAudioMessage(chatMessage, audioFile);
    }
}