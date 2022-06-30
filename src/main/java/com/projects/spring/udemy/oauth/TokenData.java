package com.projects.spring.udemy.oauth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TokenData {
    private String accessToken;

    private String refreshToken;

    private int expiresIn;

    private int refreshExpiresIn;

    private String tokenType;

}
