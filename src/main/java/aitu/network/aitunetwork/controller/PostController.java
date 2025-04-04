package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.dto.PostDTO;
import aitu.network.aitunetwork.model.entity.Post;
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
            @RequestBody PostDTO post,
            @RequestParam List<MultipartFile> files
            ){
        return postService.createPost(post, files);
    }

    @GetMapping("/{id}")
    Post getById(@PathVariable String id){
        return postService.findById(id);
    }

    @GetMapping
    List<Post> searchPosts(@RequestBody PostDTO criteria){
        return postService.searchPosts(criteria);
    }

    @PatchMapping("/{id}")
    Post updatePost(@RequestBody Map<String, String> map, @PathVariable String id){
        return postService.updateDescription(map, id);
    }

    @DeleteMapping("/{postId}")
    Post deleteFiles(@PathVariable String postId, @RequestParam List<String> fileIds){
        return postService.deleteFiles(postId, fileIds);
    }
}
