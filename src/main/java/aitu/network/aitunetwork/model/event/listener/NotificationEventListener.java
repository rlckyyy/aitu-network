package aitu.network.aitunetwork.model.event.listener;

import aitu.network.aitunetwork.model.event.listener.model.PostEvent;
import aitu.network.aitunetwork.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;
    @EventListener
    public void acceptNotificationEvent(PostEvent postEvent){
        log.info("creating notification for Post Event");
        notificationService.sendPostNotification(postEvent);
    }

}
