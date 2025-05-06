package aitu.network.aitunetwork.controller;

import aitu.network.aitunetwork.model.dto.CommentCriteria;
import aitu.network.aitunetwork.model.dto.StringWrapper;
import aitu.network.aitunetwork.model.entity.Comment;
import aitu.network.aitunetwork.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    List<Comment> searchComments(@RequestBody CommentCriteria criteria) {
        return commentService.getComments(criteria);
    }

    @PostMapping
    Comment addComment(@RequestPart Comment comment,
                       @RequestPart(required = false) List<MultipartFile> multipartFiles) {
        return commentService.addComment(comment, multipartFiles);
    }

    @PatchMapping("/{id}")
    Comment editComment(@RequestBody StringWrapper content, @PathVariable("id") String commentId) {
        return commentService.editContent(content, commentId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
    }


}
