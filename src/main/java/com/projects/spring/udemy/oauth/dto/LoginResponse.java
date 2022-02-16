package com.projects.spring.udemy.oauth.dto;

import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.oauth.TokenData;

public class LoginResponse {
    private TokenData tokenData;
    private ImageClass person;

    public LoginResponse(TokenData tokenData, ImageClass person) {
        this.tokenData = tokenData;
        this.person = person;
    }

    public TokenData getTokenData() {
        return tokenData;
    }

    public void setTokenData(TokenData tokenData) {
        this.tokenData = tokenData;
    }

    public ImageClass getPerson() {
        return person;
    }

    public void setUser(ImageClass person) {
        this.person = person;
    }
}
