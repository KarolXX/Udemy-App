package com.projects.spring.udemy.oauth;

import com.projects.spring.udemy.oauth.dto.LoginResponse;
import com.projects.spring.udemy.oauth.dto.RegisterForm;
import com.projects.spring.udemy.user.UserRepository;
import com.projects.spring.udemy.oauth.dto.LoginForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

//    @PostMapping
//    ResponseEntity<?> createUser(@RequestBody RegisterForm source) {
//        logger.info("Registering");
//        Boolean result = service.register(source);
//        return ResponseEntity.ok(result);
//    }
}
