package com.projects.spring.udemy.author;

import com.projects.spring.udemy.course.dto.FileModel;
import com.projects.spring.udemy.file.AppFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/authors")
public class AuthorController {
    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);
    private final AuthorRepository repository;
    private final AppFileService imageService;

    public AuthorController(AuthorRepository repository, AppFileService imageService) {
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

    @RolesAllowed("ROLE_ADMIN")
    @GetMapping
    ResponseEntity<List<Author>> getAllAuthors() {
            logger.warn("Exposing authors");
            var result = repository.findAll();
            return ResponseEntity.ok(result);
    }

    // responsibility for creating the author entity has been transferred to OAuthController
//    @PostMapping
//    ResponseEntity<Author> createAuthor(@RequestBody Author author) {
//        logger.info("Author has been added!");
//        var result = repository.save(author);
//        return ResponseEntity.created(URI.create("/" + result.getAuthorId())).body(result);
//    }

    @PutMapping("/{id}")
    ResponseEntity<?> uploadAuthorImage(@PathVariable Integer id, FileModel fileModel) {
        logger.info("Author's image has been set");
        return imageService.saveFile(id, fileModel, "author");
    }
}
