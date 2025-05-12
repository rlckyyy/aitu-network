package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.dto.CommentCriteria;
import aitu.network.aitunetwork.model.dto.StringWrapper;
import aitu.network.aitunetwork.model.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final FileService fileService;
    private final MongoTemplate mongoTemplate;

    public Comment addComment(Comment comment, List<MultipartFile> multipartFiles) {
        if (multipartFiles == null) {
            multipartFiles = Collections.emptyList();
        }
        var linkList = multipartFiles.stream().map(fileService::uploadFile).map(fileService::getLinkForResource).toList();
        comment.setMediaFileLinks(linkList);
        return mongoTemplate.insert(comment);
    }

    public void deleteComment(String id) {
        var query = new Query(Criteria.where("_id").is(id));
        var comment = mongoTemplate.findAndRemove(query, Comment.class);
        if (Objects.isNull(comment) || CollectionUtils.isEmpty(comment.getMediaFileLinks())) {
            return;
        }
        fileService.deleteFilesByLink(comment.getMediaFileLinks());
    }

    public Comment editContent(StringWrapper content, String commentId) {
        var query = new Query(Criteria.where("_id").is(commentId));
        var update = new Update();
        update.addToSet("content", content.content());
        return mongoTemplate.findAndModify(query, update, Comment.class);
    }

    public List<Comment> getComments(CommentCriteria criteria) {
        Query query = buildCriteria(criteria);
        return mongoTemplate.find(query, Comment.class);
    }

    private Query buildCriteria(CommentCriteria criteria) {
        var query = new Query();
        if (Objects.nonNull(criteria.postId()) && StringUtils.hasText(criteria.postId())) {
            query.addCriteria(Criteria.where(CommentCriteria.Fields.postId).is(criteria.postId()));
        }
        if (Objects.nonNull(criteria.groupId()) && StringUtils.hasText(criteria.groupId())) {
            query.addCriteria(Criteria.where(CommentCriteria.Fields.groupId).is(criteria.groupId()));
        }
        if (Objects.nonNull(criteria.userId()) && StringUtils.hasText(criteria.userId())) {
            query.addCriteria(Criteria.where(CommentCriteria.Fields.userId).is(criteria.userId()));
        }
        return query;
    }
}
