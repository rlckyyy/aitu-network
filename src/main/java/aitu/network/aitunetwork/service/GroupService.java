package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.GroupCreateDTO;
import aitu.network.aitunetwork.model.entity.Avatar;
import aitu.network.aitunetwork.model.entity.Group;
import aitu.network.aitunetwork.repository.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository repository;
    private final MongoTemplate mongoTemplate;
    private final FileService fileService;


    public Group createGroup(GroupCreateDTO dto, MultipartFile file, CustomUserDetails details) {
        if (repository.existsByName(dto.name())) {
            throw new ConflictException(String.format("Group with name %s is already exist", dto.name()));
        }
        String hexId = fileService.uploadFile(file);
        var group = Group.builder()
                .name(dto.name())
                .ownerId(details.user().getId())
                .userIds(List.of(details.user().getId()))
                .description(dto.description())
                .avatar(Avatar.builder()
                        .id(hexId)
                        .location(fileService.getLinkForResource(hexId))
                        .build())
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

    public Group followGroup(String groupId, CustomUserDetails details) {
        Group group = findById(groupId);
        group.getUserIds().add(details.user().getId());
        return repository.save(group);
    }
}
