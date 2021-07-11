package com.projects.spring.udemy.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
        var result = repository.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<User> createUser(@RequestBody User source) {
        var result = repository.save(source);
        return ResponseEntity.created(URI.create("/" + result.getUserId())).body(result);
    }

    @PatchMapping("/{id}")
    ResponseEntity<?> test(@PathVariable int id) {
        var user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No such user"));
        var courseWithId1 = user.getRatings()
                .stream().filter(rate -> rate.getId().getCourseId() == 1 )
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("This user has no course with id 1"));
        courseWithId1.setRating(10);
        return ResponseEntity.noContent().build();
    }
}
