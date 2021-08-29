package com.projects.spring.udemy.user;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.user.dto.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserRepository repository;
    private UserService service;

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

    @GetMapping("/{id}/favourite-courses")
    ResponseEntity<List<CourseInMenu>> getUserFavouriteCourses(@PathVariable Integer id) {
        logger.warn("Exposing all the user favourite courses");
        var result = service.getUserFavouriteCourses(id);
        return ResponseEntity.ok(result);
    }

    // FIXME Get method should not have Request body
    // I marked it as POST on quickly ;p
    @PostMapping("/{id}")
    ResponseEntity<?> logIn(@RequestBody LoginForm loginForm) {
        logger.info("Signing in");
        Optional<User> target = repository.findById(loginForm.getId());
        if(target.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user with given id");
        if(!target.get().getName().equals(loginForm.getName()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user with given nick");
        return ResponseEntity.ok(target);
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User source) {
        var result = service.createUser(source);
        return ResponseEntity.created(URI.create("/" + result.getUserId())).body(result);
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

    @Transactional
    @PatchMapping("/{id}")
    ResponseEntity<?> test(@PathVariable int id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));
        var courseWithId1 = user.getRatings()
                .stream().filter(rate -> rate.getId().getCourseId() == 1 )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("This user has no course with id 1"));
        courseWithId1.setRating(5.0); //FIXME - it's not reflected in DB
        return ResponseEntity.ok(courseWithId1.getRating());
    }
}
