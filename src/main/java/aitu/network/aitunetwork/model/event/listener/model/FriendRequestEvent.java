package aitu.network.aitunetwork.model.event.listener.model;

import org.springframework.context.ApplicationEvent;

public class FriendRequestEvent extends ApplicationEvent {

    public FriendRequestEvent(Object source) {
        super(source);
    }
}
