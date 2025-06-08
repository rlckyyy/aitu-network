package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.common.exception.ConflictException;
import aitu.network.aitunetwork.common.exception.EntityNotFoundException;
import aitu.network.aitunetwork.config.security.CustomUserDetails;
import aitu.network.aitunetwork.model.dto.PostDTO;
import aitu.network.aitunetwork.model.entity.Group;
import aitu.network.aitunetwork.model.entity.Post;
import aitu.network.aitunetwork.model.entity.Reaction;
import aitu.network.aitunetwork.model.entity.User;
import aitu.network.aitunetwork.model.enums.AccessType;
import aitu.network.aitunetwork.model.enums.PostType;
import aitu.network.aitunetwork.model.event.listener.model.PostEvent;
import aitu.network.aitunetwork.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService implements ApplicationContextAware {
    private final PostRepository repository;
    private final FileService fileService;
    private final MongoTemplate mongoTemplate;
    private final GroupService groupService;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;
    private Supplier<PostService> self;

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
        Post post = repository.save(postBuilder.build());
        publisher.publishEvent(new PostEvent(post));
        return post;
    }

    @Cacheable(value = "posts", key = "#id")
    public Post findById(String id) {
        log.info("test caching");
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(Post.class, id));
    }

    public List<Post> searchPosts(String ownerId, String groupId, PostType postType, String description) {
        Query query = new Query();
        List<Criteria> criteriaList = new ArrayList<>();

        if (StringUtils.isNotBlank(ownerId)) {
            criteriaList.add(Criteria.where("ownerId").is(ownerId));
        }

        if (StringUtils.isNotBlank(groupId)) {
            criteriaList.add(Criteria.where("groupId").is(groupId));
        }

        if (postType != null) {
            criteriaList.add(Criteria.where("postType").is(postType));
        }

        if (StringUtils.isNotBlank(description)) {
            criteriaList.add(Criteria.where("description").regex(".*" + description + ".*", "i"));
        }
        if (StringUtils.isBlank(ownerId) && StringUtils.isBlank(groupId)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                Set<String> publicOwnerIds = Stream.concat(
                        userService.fetchUsersByAccessType(AccessType.PUBLIC).stream().map(User::getId),
                        groupService.fetchGroupsByAccessType(AccessType.PUBLIC).stream().map(Group::getId)
                ).collect(Collectors.toSet());

                criteriaList.add(Criteria.where("ownerId").in(publicOwnerIds));
            } else {
                User user = userService.getById(auth.getName());
                criteriaList.add(buildAccessCriteria(user));
            }
        }

        query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        return mongoTemplate.find(query, Post.class);
    }

    @CachePut(value = "posts", key = "#id")
    public Post updateDescription(Map<String, String> map, String id) {
        Post post = self.get().findById(id);
        String description = map.get("description");
        if (Objects.isNull(description) || description.isBlank()) {
            throw new ConflictException("Post content can not be blank or null");
        }
        post.setDescription(description);
        return repository.save(post);
    }

    public Post deleteFiles(String postId, List<String> fileIds) {
        Post post = self.get().findById(postId);
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

    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Post post = mongoTemplate.findAndRemove(query, Post.class);
        if (post == null) {
            throw new EntityNotFoundException(Post.class, id);
        }
        fileService.deleteFilesByLink(post.getMediaFileIds());
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

    private Criteria buildAccessCriteria(User user) {
        Set<String> allowedOwnerIds = new HashSet<>();

        allowedOwnerIds.addAll(
                userService.fetchUsersByAccessType(AccessType.PUBLIC)
                        .stream().map(User::getId).toList()
        );
        allowedOwnerIds.addAll(user.getFriendList());

        allowedOwnerIds.addAll(
                groupService.fetchGroupsByAccessType(AccessType.PUBLIC)
                        .stream().map(Group::getId).toList()
        );
        allowedOwnerIds.addAll(
                groupService.findByUserIdsContaining(user.getId())
                        .stream().map(Group::getId).toList()
        );

        allowedOwnerIds.add(user.getId());

        return Criteria.where("ownerId").in(allowedOwnerIds);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.self = () -> applicationContext.getBean(PostService.class);
    }
}
