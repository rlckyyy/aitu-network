package aitu.network.aitunetwork.controller.ws;

import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatService chatService;

    @MessageMapping("/chat")
    public void processMessage(
            @Payload @Valid ChatMessage newChatMessage
    ) {
        chatService.processMessage(newChatMessage);
    }

    @MessageMapping("/chat/message/{messageId}")
    public void processMessageStatus(
            @DestinationVariable("messageId") String messageId
    ) {
        chatService.processMessageStatus(messageId);
    }
}
