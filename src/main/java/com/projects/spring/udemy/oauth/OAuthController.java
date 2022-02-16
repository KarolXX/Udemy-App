package com.projects.spring.udemy.oauth;

import com.projects.spring.udemy.oauth.dto.AuthorForm;
import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.UserForm;
import com.projects.spring.udemy.user.UserRepository;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/oauth")
public class OAuthController {
    private static final Logger logger = LoggerFactory.getLogger(OAuthController.class);
    private UserRepository repository;
    private OAuthService service;

    public OAuthController(UserRepository repository, OAuthService service) {
        this.repository = repository;
        this.service = service;
    }

    @PostMapping("/login")
    ResponseEntity<?> logIn(@RequestBody UserForm userForm) {
        logger.info("Signing in");
        LoginResponse result = service.login(userForm);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/users")
    ResponseEntity<?> createUser(@RequestBody UserForm source) {
        logger.info("User registration");
        LoginResponse result = service.register(source);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/authors")
    ResponseEntity<?> createAuthor(@RequestBody AuthorForm source) {
        logger.info("Author registration");
        LoginResponse result = service.register(source);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserRepresentation>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }

    @ExceptionHandler(NickAlreadyExistsException.class)
    ResponseEntity<?> illegalNickHandler(NickAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
    }

    @ExceptionHandler(BadRequestException.class)
    ResponseEntity<?> illegalLoginDataHandler(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(InternalServerErrorException.class)
    ResponseEntity<?> serverErrorHandler(InternalServerErrorException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error...");
    }
}
