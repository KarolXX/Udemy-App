package com.projects.spring.udemy.comment;

import com.projects.spring.udemy.course.dto.CommentWithUserID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@CrossOrigin
@RestController
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private CommentService service;
    private CommentRepository repository;
    private final String basePath = "/courses/{courseId}/comments";

    public CommentController(CommentService service, CommentRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    @PostMapping(path = basePath)
    ResponseEntity<?> createComment(
            @PathVariable Integer courseId,
            @RequestBody CommentWithUserID comment
    ) {
        logger.info("New comment has been added");
        Optional<Comment> result = service.createComment(courseId, comment);
        if(result.isPresent())
            return ResponseEntity.created(URI.create("/" + result.get().getCommentId())).body(result);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Buy course first and then you can comment it");
    }

    @PutMapping(path = basePath + "/{commentId}")
    ResponseEntity<?> editComment(@PathVariable Integer commentId, @RequestBody Comment source) {
        logger.warn("Comment has been edited");
        service.editComment(commentId, source);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = basePath + "/{commentId}")
    ResponseEntity<?> deleteComment(@PathVariable Integer commentId) {
        logger.warn("Comment has been deleted");
        service.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }
}
