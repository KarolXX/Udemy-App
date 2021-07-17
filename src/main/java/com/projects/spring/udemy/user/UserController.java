package com.projects.spring.udemy.user;

import com.projects.spring.udemy.user.dto.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private UserRepository repository;

    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<User>> getUsers() {
        logger.warn("Exposing all the users");
        var result = repository.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getUser(@RequestBody LoginForm loginForm) {
        logger.info("Signing in");
        var target = repository.findById(loginForm.getId())
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
        if(!target.getName().equals(loginForm.getName()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user with given nick");
        return ResponseEntity.ok(target);
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User source) {
        var result = repository.save(source);
        return ResponseEntity.created(URI.create("/" + result.getUserId())).body(result);
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
