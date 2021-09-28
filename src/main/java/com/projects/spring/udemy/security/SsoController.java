package com.projects.spring.udemy.security;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class SsoController {
    @GetMapping("/logout")
    void logout(HttpServletRequest request) throws ServletException {
        request.logout();
    }
}
