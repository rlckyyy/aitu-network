package aitu.network.aitunetwork.listener;

import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.service.ChatUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatAppEventListener {

    private final ChatUserService chatUserService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        CustomUserDetails userDetails = ((CustomUserDetails) ((UsernamePasswordAuthenticationToken) event.getUser()).getPrincipal());
        chatUserService.connectChatUser(userDetails.getUser());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        CustomUserDetails userDetails = ((CustomUserDetails) ((UsernamePasswordAuthenticationToken) event.getUser()).getPrincipal());
        chatUserService.disconnectChatUser(userDetails.getUser());
    }
}
