package com.projects.spring.udemy.author;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private AuthorRepository repository;
    private AuthorService service;

    public AuthorController(AuthorRepository repository, AuthorService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping("/{id}")
    ResponseEntity<Author> getAuthor(@PathVariable Integer id) {
        logger.warn("Author exposing");
        var result = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No author with given id"));
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        logger.info("Author has been added!");
        var result = repository.save(author);
        return ResponseEntity.created(URI.create("/" + result.getAuthorId())).body(result);
    }

//    @PutMapping("/{id}")
//    ResponseEntity<?> uploadAuthorImage() {
//        logger.info("Author's image has been set");
//    }
}
