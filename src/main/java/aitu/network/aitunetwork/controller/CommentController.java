package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.entity.Comment;
import aitu.network.aitunetwork.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @PatchMapping("/comments")
    Comment addComment(@RequestPart Comment comment,
                    @RequestPart List<MultipartFile> multipartFiles) {
        return commentService.addComment(comment, multipartFiles);
    }


}
