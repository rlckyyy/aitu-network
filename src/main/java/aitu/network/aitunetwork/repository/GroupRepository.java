package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.Group;
import aitu.network.aitunetwork.model.enums.AccessType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;

public interface GroupRepository extends MongoRepository<Group, String> {
    boolean existsByName(String name);

    Collection<Group> findByUserIdsContains(String userId);

    Collection<Group> findByAccessType(AccessType accessType);
}
