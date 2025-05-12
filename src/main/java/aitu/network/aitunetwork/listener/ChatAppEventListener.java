package aitu.network.aitunetwork.listener;

import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.service.UserService;
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
    private final UserService userService;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        var authentication = (UsernamePasswordAuthenticationToken) event.getUser();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails(User user)) {
            userService.connectUser(user);
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        var authentication = (UsernamePasswordAuthenticationToken) event.getUser();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails(User user)) {
            userService.disconnectUser(user);
        }
    }
}
