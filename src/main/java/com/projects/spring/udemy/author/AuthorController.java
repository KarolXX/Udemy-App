package com.projects.spring.udemy.author;

import com.projects.spring.udemy.course.dto.ImageModel;
import com.projects.spring.udemy.file.AppImageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private AuthorRepository repository;
    private AppImageService imageService;

    public AuthorController(AuthorRepository repository, AppImageService imageService) {
        this.repository = repository;
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    ResponseEntity<Author> getAuthor(@PathVariable Integer id) {
        logger.warn("Author exposing");
        var result = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No author with given id"));
        return ResponseEntity.ok(result);
    }

    @GetMapping
    ResponseEntity<List<Author>> getAllAuthors(Authentication auth) {
//        if(auth.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            logger.warn("Exposing authors");
            var result = repository.findAll();
            return ResponseEntity.ok(result);
//        }
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    @PostMapping
    ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        logger.info("Author has been added!");
        var result = repository.save(author);
        return ResponseEntity.created(URI.create("/" + result.getAuthorId())).body(result);
    }

    @PutMapping("/{id}")
    ResponseEntity<?> uploadAuthorImage(@PathVariable Integer id, ImageModel imageModel) {
        logger.info("Author's image has been set");
        return imageService.saveImage(id, imageModel, "author");
    }
}
