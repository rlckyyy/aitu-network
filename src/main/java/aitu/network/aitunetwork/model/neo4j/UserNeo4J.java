package aitu.network.aitunetwork.model.neo4j;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserNeo4J {
    @Id
    private String email;
    private String username;
    @Relationship(type = "FRIENDS", direction = Relationship.Direction.OUTGOING)
    private Set<UserNeo4J> friendsOut = new HashSet<>();

    @Relationship(type = "FRIENDS", direction = Relationship.Direction.INCOMING)
    private Set<UserNeo4J> friendsIn = new HashSet<>();
}
