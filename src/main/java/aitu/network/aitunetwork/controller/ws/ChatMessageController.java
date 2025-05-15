package aitu.network.aitunetwork.controller.ws;

import aitu.network.aitunetwork.model.dto.chat.MessageMark;
import aitu.network.aitunetwork.model.entity.chat.ChatMessage;
import aitu.network.aitunetwork.service.ChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Slf4j
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

    @MessageMapping("/chat/message/status")
    public void processMessageStatus(@Payload MessageMark messageMark) {
        log.info("Message with id {} marked as read", messageMark.getMessageIds());
        chatService.processMessageStatus(messageMark);
    }
}
