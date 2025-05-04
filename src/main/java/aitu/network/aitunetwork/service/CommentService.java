package aitu.network.aitunetwork.service;

import aitu.network.aitunetwork.model.entity.Comment;
import aitu.network.aitunetwork.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final PostService postService;
    private final FileService fileService;
    private final MongoTemplate mongoTemplate;

    public Comment addComment(Comment comment, List<MultipartFile> multipartFiles) {
        List<String> linkList = multipartFiles.stream().map(fileService::uploadFile).map(fileService::getLinkForResource).toList();
        comment.setMediaFileLinks(linkList);
        return mongoTemplate.insert(comment);
    }

    public void deleteComment(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        mongoTemplate.remove(query, Comment.class);
    }
}
