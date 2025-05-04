package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepository extends MongoRepository<Comment, String> {
}
