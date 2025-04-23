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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload @Valid ChatMessage newChatMessage
    ) {
        chatService.processMessage(newChatMessage);
    }

    @ResponseBody
    @GetMapping("/messages/{chatId}/count")
    public ResponseEntity<Map<String, Object>> countNewMessages(
            @PathVariable String chatId
    ) {
        return ResponseEntity.ok(chatService.countNewMessages(chatId));
    }

    @ResponseBody
    @GetMapping("/messages/{chatId}")
    public List<ChatMessage> findChatMessages(@PathVariable String chatId) {
        return chatService.findChatMessages(chatId);
    }

    @ResponseBody
    @GetMapping("/rooms/{id}")
    public List<ChatRoomDTO> getUserChatRooms(@PathVariable String id) {
        return chatService.getUserChatRooms(id);
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
    @PatchMapping("/rooms/{chatRoomId}/{participantId}")
    public void addUserToChatRoom(@PathVariable String chatRoomId, @PathVariable String participantId) {
        chatService.addParticipantToChatRoom(chatRoomId, participantId);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/rooms/{chatRoomId}/{participantId}")
    public void deleteUserFromChatRoom(@PathVariable String chatRoomId, @PathVariable String participantId) {
        chatService.deleteUserFromChatRoom(chatRoomId, participantId);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/messages/files")
    public ChatMessage saveAudioMessage(
            @Valid @RequestPart("chatMessage") ChatMessage chatMessage,
            @RequestPart("audioFile") MultipartFile audioFile
    ) {
        return chatService.saveAudioMessage(chatMessage, audioFile);
    }
}