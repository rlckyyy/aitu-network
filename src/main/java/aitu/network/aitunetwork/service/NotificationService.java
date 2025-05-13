package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.Notification;
import aitu.network.aitunetwork.model.entity.Post;
import aitu.network.aitunetwork.model.enums.IssuerType;
import aitu.network.aitunetwork.model.enums.PostType;
import aitu.network.aitunetwork.model.event.listener.model.PostEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final MongoTemplate mongoTemplate;

    public void sendPostNotification(PostEvent postEvent) {
        Post post = (Post) postEvent.getSource();
        createNotificationFromPost(post)
                .ifPresent(notification -> {
                    mongoTemplate.save(notification);
                    log.info("Notification saved: {}", notification);
                });
    }

    private Optional<Notification> createNotificationFromPost(Post post) {
        String resource = post.getResource();
        if (resource == null || resource.isBlank()) {
            return Optional.empty();
        }
        String description = Objects.toString(post.getDescription(), "");
        String noticeContent = String.format("""
                %s posted a publication: %s
                """, resource, description);

        Notification.NotificationBuilder builder = Notification.builder()
                .content(noticeContent)
                .isNotified(false)
                .issuerId(post.getOwnerId());

        buildPostNotification(builder, post);
        return Optional.of(builder.build());
    }

    private void buildPostNotification(Notification.NotificationBuilder builder, Post post) {
        builder.issuerType(post.getPostType() == PostType.USER ? IssuerType.USER : IssuerType.GROUP);
    }
}
