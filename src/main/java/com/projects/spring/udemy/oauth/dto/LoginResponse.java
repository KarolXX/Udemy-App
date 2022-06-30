package com.projects.spring.udemy.oauth.dto;

import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.oauth.TokenData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private TokenData tokenData;
    private ImageClass person;
}
