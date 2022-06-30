package com.projects.spring.udemy.user;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserRepository repository;
    private final UserService service;

    public UserController(UserRepository repository, UserService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<User>> getUsers() {
        logger.warn("Exposing all the users");
        var result = repository.findAll();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        logger.warn("User with " + id + "deleted");
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/favourite-courses")
    ResponseEntity<List<CourseInMenu>> getUserFavouriteCourses(@PathVariable Integer id) {
        logger.warn("Exposing all the user favourite courses");
        var result = service.getUserFavouriteCourses(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{userId}/courses/{courseId}/course-liking")
    ResponseEntity<?> likeCourse(@PathVariable Integer userId, @PathVariable Integer courseId) {
        logger.info("User liked the course");
        repository.likeCourse(userId, courseId);
        return ResponseEntity.created(URI.create("/")).build();
    }

    @DeleteMapping("/{userId}/courses/{courseId}/course-disliking")
    ResponseEntity<?> dislikeCourse(@PathVariable Integer userId, @PathVariable Integer courseId) {
        logger.info("User disliked course");
        repository.dislikeCourse(userId, courseId);
        return ResponseEntity.noContent().build();
    }
}
