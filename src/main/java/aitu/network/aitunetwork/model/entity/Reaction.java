package aitu.network.aitunetwork.model.entity;

import aitu.network.aitunetwork.model.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Reaction {
    private String userId;
    private ReactionType reactionType;
}
