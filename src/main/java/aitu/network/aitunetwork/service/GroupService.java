package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.GroupCreateDTO;
import aitu.network.aitunetwork.model.entity.Group;
import aitu.network.aitunetwork.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final PostService postService;
    private final MongoTemplate mongoTemplate;


    public Group createGroup(GroupCreateDTO dto, CustomUserDetails details) {
        if (repository.existsByName(dto.name())) {
            throw new ConflictException(String.format("Group with name %s is already exist", dto.name()));
        }
        var group = Group.builder()
                .name(dto.name())
                .ownerId(details.getUser().getId())
                .userIds(List.of(details.getUser().getId()))
                .description(dto.description())
                .type(dto.accessType())
                .build();
        return repository.save(group);
    }

    public List<Group> searchGroups(String name, String ownerId, String userId) {
        var query = new Query();
        if (name != null && !name.isEmpty()) {
            query.addCriteria(Criteria.where("name").regex(name, "i"));
        }
        if (ownerId != null && !ownerId.isEmpty()) {
            query.addCriteria(Criteria.where("ownerId").is(ownerId));
        }
        if (userId != null && !userId.isEmpty()) {
            query.addCriteria(Criteria.where("userIds").in(userId));
        }
        return mongoTemplate.find(query, Group.class);
    }

    public Group findById(String id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(Group.class, id));
    }
}
