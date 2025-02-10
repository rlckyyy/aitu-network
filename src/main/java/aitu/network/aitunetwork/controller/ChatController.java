package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.dto.ChatUserDTO;
import aitu.network.aitunetwork.model.entity.ChatMessage;
import aitu.network.aitunetwork.model.entity.ChatNotification;
import aitu.network.aitunetwork.model.entity.ChatRoom;
import aitu.network.aitunetwork.service.ChatMessageService;
import aitu.network.aitunetwork.service.ChatRoomService;
import aitu.network.aitunetwork.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;
    private final ChatUserService chatUserService;

    @MessageMapping("/chat")
    @SendTo("user/topic/messages")
    public void processMessage(
            @Payload ChatMessage chatMessage
    ) {
        var chatId = chatRoomService.getChatId(chatMessage.getSender(), chatMessage.getRecipient(), true);
        chatMessage.setChatId(chatId.get());

        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipient(), "/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getChatId(),
                        saved.getSender(),
                        saved.getContent()
                ));
    }

    @MessageMapping("/user.addUser")
    @SendTo("/user/public")
    public ChatUserDTO addUser(
            @Payload ChatUserDTO chatUserDTO
    ) {
        return chatUserDTO;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/public")
    public ChatUserDTO disconnect(
            @Payload ChatUserDTO chatUserDTO
    ) {
        return chatUserDTO;
    }

    @GetMapping("/messages/{senderId}/{recipientId}/count")
    public ResponseEntity<Long> countNewMessages(
            @PathVariable String senderId,
            @PathVariable String recipientId) {

        return ResponseEntity
                .ok(chatMessageService.countNewMessages(senderId, recipientId));
    }

    @GetMapping("/messages/{sender}/{recipient}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable String sender,
                                                              @PathVariable String recipient) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(sender, recipient));
    }

//    @GetMapping("/messages/{id}")
//    public ResponseEntity<?> findMessage(@PathVariable String id) {
//        return ResponseEntity
//                .ok(chatMessageService.findById(id));
//    }


    @MessageMapping("/message") // app/message
    @SendTo("/chatroom/public")
    public ChatMessage receivePublicMessage(
            @Payload ChatMessage message
    ) {
        return message;
    }

    @MessageMapping("/private-message")
    public ChatMessage receivePrivateMessage(
            @Payload ChatMessage message
    ) {
        chatRoomService.getChatId(message.getSender(), message.getRecipient(), true)
                .ifPresent(message::setChatId);

        ChatMessage savedChatMessage = chatMessageService.save(message);
        messagingTemplate.convertAndSendToUser(
                message.getRecipient(),
                "/private",
                new ChatNotification(
                        savedChatMessage.getId(),
                        savedChatMessage.getChatId(),
                        savedChatMessage.getSender(),
                        savedChatMessage.getContent()
                )
        ); // receiver should listen to /user/<receiverId>/private to get messages
        return message;
    }

    @ResponseBody
    @GetMapping("/chats/rooms/{username}")
    public List<ChatRoom> getUserChatHistory(@PathVariable String username) {
        return chatUserService.getUserChats(username);
    }

    @ResponseBody
    @GetMapping("/messages/{chatId}")
    public List<ChatMessage> getChatMessages(@PathVariable String chatId) {
        return chatMessageService.findChatMessages(chatId);
    }
}