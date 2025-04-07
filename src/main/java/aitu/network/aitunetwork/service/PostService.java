package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.model.dto.PostDTO;
import aitu.network.aitunetwork.model.entity.Post;
import aitu.network.aitunetwork.model.enums.PostType;
import aitu.network.aitunetwork.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
                .postType(postDTO.postType())
                .description(postDTO.description())
                .build();
        if (files != null) {
            List<String> idList = files.stream().map(fileService::uploadFile).map(fileService::getLinkForResource).toList();
            post.setMediaFileIds(idList);
        }
        return repository.save(post);
    }

    public Post findById(String id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(Post.class, id));
    }

    public List<Post> searchPosts(String ownerId,
                                  String groupId,
                                  PostType postType,
                                  String description) {
        Query query = new Query();
        if (StringUtils.isNotBlank(ownerId)) {
            query.addCriteria(Criteria.where("ownerId").is(ownerId));
        }
        if (StringUtils.isNotBlank(groupId)) {
            query.addCriteria(Criteria.where("groupId").is(groupId));
        }
        if (postType != null) {
            query.addCriteria(Criteria.where("postType").is(postType));
        }
        if (StringUtils.isNotBlank(description)) {
            query.addCriteria(Criteria.where("description").regex(".*" + description + ".*", "i"));
        }
        return mongoTemplate.find(query, Post.class);
    }

    public Post updateDescription(Map<String, String> map, String id) {
        Post post = findById(id);
        String description = map.get("description");
        if (Objects.isNull(description) || description.isBlank()) {
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
