package com.projects.spring.udemy.oauth.dto;

import com.projects.spring.udemy.oauth.TokenData;
import com.projects.spring.udemy.user.User;

public class LoginResponse {
    private TokenData tokenData;
    private User user;

    public LoginResponse(TokenData tokenData, User user) {
        this.tokenData = tokenData;
        this.user = user;
    }

    public TokenData getTokenData() {
        return tokenData;
    }

    public void setTokenData(TokenData tokenData) {
        this.tokenData = tokenData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
