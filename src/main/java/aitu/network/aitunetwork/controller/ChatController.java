package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.entity.ChatMessage;
import aitu.network.aitunetwork.model.entity.ChatRoom;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.ChatMessageService;
import aitu.network.aitunetwork.service.ChatRoomService;
import aitu.network.aitunetwork.service.ChatUserService;
import aitu.network.aitunetwork.service.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final ChatUserService chatUserService;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ) {
        var chatId = chatRoomService.getChatId(chatMessage.getSender(), chatMessage.getRecipient(), true);
        chatMessage.setChatId(chatId.get());
        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(), "/queue/messages",
                saved
        );
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Map<String, Long>> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        long count = chatMessageService.countNewMessages(senderId, recipientId);
        return ResponseEntity
                .ok(Map.of("count", count));
    }

    @GetMapping("/messages/{sender}/{recipient}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String sender,
                                                              @PathVariable String recipient) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(sender, recipient));
    }

    @ResponseBody
    @GetMapping("/rooms/{email}")
    public List<ChatRoom> getUserChatHistory(@PathVariable String email) {
        return chatUserService.getUserChats(email);
    }

    @ResponseBody
    @GetMapping("/users/online")
    public List<User> getOnlineUsers() {
        return chatUserService.getOnlineUsers();
    }

    @ResponseBody
    @GetMapping("/users/search")
    public List<User> searchUsers(@RequestParam String query) {
        return chatUserService.searchUsers(query);
    }

    @ResponseBody
    @GetMapping("/id/{sender}/{recipient}")
    public Map<String, String> getChatId(@PathVariable String sender, @PathVariable String recipient) {
        Optional<String> maybeChatId = chatRoomService.getChatId(sender, recipient, false);
        return maybeChatId.map(chatId -> Map.of("chatId", chatId)).orElseGet(() -> Map.of("chatId", ChatUtils.generateChatId(sender, recipient)));
    }
}