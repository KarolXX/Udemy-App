package com.projects.spring.udemy.comment;

import com.projects.spring.udemy.course.dto.CommentWithUserID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Set;

@RestController
public class CommentController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    private CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @PostMapping(path = "courses/{courseId}/comments")
    ResponseEntity<Comment> createComment(
            @PathVariable Integer courseId,
            @RequestBody CommentWithUserID comment
    ) {
        logger.info("New comment has been added");
        var result = service.createComment(courseId, comment);
        return ResponseEntity.ok(result);
    }
}
