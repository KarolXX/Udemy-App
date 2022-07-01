package com.projects.spring.udemy;

import com.projects.spring.udemy.course.NotEnoughMoneyAvailableException;
import com.projects.spring.udemy.oauth.NickAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.InternalServerErrorException;

@RestControllerAdvice()
public class ExceptionsControllerAdvice {
    @ExceptionHandler(NotEnoughMoneyAvailableException.class)
    ResponseEntity<?> illegalNickHandler(NotEnoughMoneyAvailableException e) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getMessage());
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

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<?> illegalArgumentHandler(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
