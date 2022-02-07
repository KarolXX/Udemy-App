package com.projects.spring.udemy.oauth;

public class NickAlreadyExistsException extends RuntimeException{
    public NickAlreadyExistsException(String m) {
        super(m);
    }
}
