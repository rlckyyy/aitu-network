package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);

    List<User> findAllByEmailContainsIgnoreCaseOrUsernameContainsIgnoreCase(String query, String username);

    Optional<User> findByIdOrEmail(String id, String email);

    default Optional<User> findByIdOrEmail(String idOrEmail) {
        return findByIdOrEmail(idOrEmail, idOrEmail);
    }
}
