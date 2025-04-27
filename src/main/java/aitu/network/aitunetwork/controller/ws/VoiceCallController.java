package aitu.network.aitunetwork.controller.ws;

import aitu.network.aitunetwork.model.dto.chat.SignalMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class VoiceCallController {
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/signaling/{receiverId}")
    public void handleSignal(
            @DestinationVariable("receiverId") String receiverId,
            @Payload SignalMessage signalMessage
    ) {
        messagingTemplate.convertAndSendToUser(
                receiverId,
                "/queue/signaling",
                signalMessage
        );
    }
}
