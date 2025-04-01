package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.model.dto.PostDTO;
import aitu.network.aitunetwork.model.entity.Post;
import aitu.network.aitunetwork.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import aitu.network.aitunetwork.common.exception.ConflictException;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final FileService fileService;
    private final MongoTemplate mongoTemplate;

    public Post createPost(PostDTO postDTO, List<MultipartFile> files) {
        Post post = Post.builder()
                .ownerId(postDTO.ownerId())
                .groupId(postDTO.groupId())
                .description(postDTO.description())
                .build();
        List<String> idList = files.stream().map(fileService::uploadFile).toList();
        post.setMediaFileIds(idList);
        repository.save(post);
        return post;
    }

    public Post findById(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(Post.class, id));
    }

    public List<Post> searchPosts(PostDTO criteria) {
        Query query = new Query();
        if (criteria.ownerId() != null && StringUtils.isNotBlank(criteria.ownerId())) {
            query.addCriteria(Criteria.where("ownerId").is(criteria.ownerId()));
        }
        if (criteria.groupId() != null && StringUtils.isNotBlank(criteria.groupId())) {
            query.addCriteria(Criteria.where("groupId").is(criteria.groupId()));
        }
        if (criteria.description() != null && StringUtils.isNotBlank(criteria.description())) {
            query.addCriteria(Criteria.where("description").regex(".*" + criteria.description() + ".*", "i"));
        }
        return mongoTemplate.find(query, Post.class);
    }

    public Post updateDescription(Map<String, String> map, String id) {
        Post post = findById(id);
        String description = map.get("description");
        if (Objects.isNull(description) || description.isBlank()){
            throw new ConflictException("Post content can not be blank or null");
        }
        post.setDescription(description);
        return repository.save(post);
    }

    public Post deleteFiles(String postId, List<String> fileIds) {
        Post post = findById(postId);
        List<String> list = post
                .getMediaFileIds()
                .stream()
                .filter(r -> !fileIds.contains(r))
                .toList();
        post.setMediaFileIds(list);
        fileIds.forEach(fileService::deleteFile);
        return repository.save(post);
    }
}
