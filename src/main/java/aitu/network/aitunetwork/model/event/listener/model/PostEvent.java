package aitu.network.aitunetwork.model.event.listener.model;

import org.springframework.context.ApplicationEvent;


public class PostEvent extends ApplicationEvent {

    public PostEvent(Object entity) {
        super(entity);
    }
}
