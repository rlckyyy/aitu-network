package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.PostType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Post extends BaseEntity {
    @Id
    private String id;
    private String ownerId;
    private String groupId;
    private String resource;
    private PostType postType;
    private String description;
    private List<String> mediaFileIds;
}
