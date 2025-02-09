package aitu.network.aitunetwork.repository.neo4j;

import aitu.network.aitunetwork.model.neo4j.UserNeo4J;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.Set;

public interface UserRepository extends Neo4jRepository<UserNeo4J, String> {
    @Query("MATCH (user1:UserNeo4J {email: $email1})-[:FRIENDS]->(friend:UserNeo4J)" +
            "<-[:FRIENDS]-(user2:UserNeo4J {email: $email2}) RETURN friend")
    Set<UserNeo4J> findMutualFriends(String email1, String email2);
}
