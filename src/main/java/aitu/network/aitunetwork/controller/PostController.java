package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.dto.PostDTO;
import aitu.network.aitunetwork.model.entity.Post;
import aitu.network.aitunetwork.model.enums.PostType;
import aitu.network.aitunetwork.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {
    private final PostService postService;

    @PostMapping
    Post createPost(
            @RequestPart(required = false) PostDTO post,
            @RequestPart(required = false) List<MultipartFile> files
    ) {
        return postService.createPost(post, files);
    }

    @GetMapping("/{id}")
    Post getById(@PathVariable String id) {
        return postService.findById(id);
    }

    @GetMapping
    List<Post> searchPosts(@RequestParam(required = false) String ownerId,
                           @RequestParam(required = false) String groupId,
                           @RequestParam(required = false) PostType postType,
                           @RequestParam(required = false) String description
    ) {
        return postService.searchPosts(ownerId, groupId, postType, description);
    }

    @PatchMapping("/{id}")
    Post updatePost(@RequestBody Map<String, String> map, @PathVariable String id) {
        return postService.updateDescription(map, id);
    }

    @DeleteMapping("/{postId}")
    Post deleteFiles(@PathVariable String postId, @RequestParam List<String> fileIds) {
        return postService.deleteFiles(postId, fileIds);
    }
}
