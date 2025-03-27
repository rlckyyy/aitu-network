package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.Group;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GroupRepository extends MongoRepository<Group, String> {
    boolean existsByName(String name);
}
