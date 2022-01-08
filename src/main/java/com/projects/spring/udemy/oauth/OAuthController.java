package com.projects.spring.udemy.oauth;

import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.RegisterForm;
import com.projects.spring.udemy.user.UserRepository;
import com.projects.spring.udemy.oauth.dto.LoginForm;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    ResponseEntity<?> logIn(@RequestBody LoginForm loginForm) {
        logger.info("Signing in");
        LoginResponse response = service.login(loginForm);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    ResponseEntity<?> createUser(@RequestBody RegisterForm source) {
        logger.info("Registering");
        Boolean result = service.register(source);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/users")
    ResponseEntity<List<UserRepresentation>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}
