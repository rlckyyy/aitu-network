package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.PostDTO;
import aitu.network.aitunetwork.model.entity.Post;
import aitu.network.aitunetwork.model.entity.Reaction;
import aitu.network.aitunetwork.model.enums.PostType;
import aitu.network.aitunetwork.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository repository;
    private final FileService fileService;
    private final MongoTemplate mongoTemplate;
    private final GroupService groupService;

    public Post createPost(PostDTO postDTO, List<MultipartFile> files, CustomUserDetails userDetails) {
        Post.PostBuilder postBuilder = Post.builder()
                .ownerId(postDTO.ownerId())
                .groupId(postDTO.groupId())
                .postType(postDTO.postType())
                .description(postDTO.description());

        switch (postDTO.postType()) {
            case USER -> enrichUserPost(postBuilder, userDetails);
            case GROUP -> enrichGroupPost(postBuilder, postDTO.groupId(), userDetails);
        }

        if (files != null && !files.isEmpty()) {
            List<String> mediaLinks = files.stream()
                    .map(fileService::uploadFile)
                    .map(fileService::getLinkForResource)
                    .toList();
            postBuilder.mediaFileIds(mediaLinks);
        }
        return repository.save(postBuilder.build());
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

    private void enrichUserPost(Post.PostBuilder builder, CustomUserDetails userDetails) {
        builder.resource(userDetails.getUsername());
    }


    public void deletePost(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Post post = mongoTemplate.findAndRemove(query, Post.class);
        fileService.deleteFilesByLink(Objects.requireNonNull(post).getMediaFileIds());
    }

    public void reactToPost(Reaction reaction) {
        Query query = new Query(Criteria.where("postId")
                .is(reaction.getPostId()).and("userId").is(reaction.getUserId()));
        Update update = new Update().set("reactionType", reaction.getReactionType())
                .set("postId", reaction.getPostId())
                .set("userId", reaction.getUserId());
        mongoTemplate.upsert(query, update, Reaction.class);
    }

    public void deleteReaction(String postId, String userId) {
        Criteria criteria = Criteria
                .where("postId").is(postId)
                .and("userId").is(userId);
        Query query = new Query(criteria);
        mongoTemplate.remove(query, Reaction.class);
    }

    public List<Reaction> fetchReactions(String postId) {
        Query query = new Query(Criteria.where("postId").is(postId));
        return mongoTemplate.find(query, Reaction.class);
    }

    private void enrichGroupPost(Post.PostBuilder builder, String groupId, CustomUserDetails userDetails) {
        var group = groupService.findById(groupId);
        group.getAdminIds().stream()
                .filter(id -> id.equals(userDetails.user().getId()))
                .findAny().orElseThrow(() -> new ConflictException("errors.409.resource.owner"));
        builder.resource(group.getName());
    }

}
