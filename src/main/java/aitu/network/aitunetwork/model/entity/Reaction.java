package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.ReactionType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document
@CompoundIndex(name = "post_user_unique_idx", def = "{'postId': 1, 'userId': 1}", unique = true)
public class Reaction {
    @Id
    private String id;
    private String postId;
    private String userId;
    private ReactionType reactionType;
}
