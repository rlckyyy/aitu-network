package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SecureTalkUserRepository extends MongoRepository<User, String> {
    Optional<User> findUserByEmail(String email);
}
