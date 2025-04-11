package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SecureTalkUserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);
    List<User> findAllByEmailContainsIgnoreCaseOrUsernameContainsIgnoreCase(String query, String username);

    @Query("{ '$or':  [ {_id: ?0}, {email: ?0} ] }")
    Optional<User> findByIdOrEmail(String idOrEmail);
}
