package com.projects.spring.udemy.oauth;

import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.UserForm;
import com.projects.spring.udemy.user.UserRepository;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        LoginResponse response = service.login(userForm);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<?> createUser(@RequestBody UserForm source) {
        logger.info("Registering");
        Optional<LoginResponse> result = service.register(source);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserRepresentation>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}
