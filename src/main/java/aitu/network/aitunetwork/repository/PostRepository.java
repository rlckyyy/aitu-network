package aitu.network.aitunetwork.repository;

import aitu.network.aitunetwork.model.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PostRepository extends MongoRepository<Post, String> {
}
