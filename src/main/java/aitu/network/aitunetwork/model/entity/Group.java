package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.AccessType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Group extends BaseEntity{
    @Id
    private String id;
    @Indexed(unique = true)
    private String name;
    private String description;
    private List<String> userIds;
    private String ownerId;
    private List<String> adminIds;
    private List<String> postIds;
    private AccessType type;
    private Avatar avatar;
}
