package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SecureTalkUserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);
    List<User> findAllByEmailContainsIgnoreCase(String query);

    List<User> findAllByEmailIn(Collection<String> emails);

    @Query("{ '$or':  [ {_id: ?0}, {email: ?0} ] }")
    Optional<User> findByIdOrEmail(String idOrEmail);
}
