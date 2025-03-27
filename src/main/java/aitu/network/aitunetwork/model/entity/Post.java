package aitu.network.aitunetwork.model.entity;

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
    private String description;
    private List<String> mediaFileIds;
}
