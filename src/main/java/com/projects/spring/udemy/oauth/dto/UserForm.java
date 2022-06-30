package com.projects.spring.udemy.oauth.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserForm {
    @NotBlank(message = "Please enter your name")
    private String name;
    @NotBlank(message = "Please enter your name")
    @Setter(AccessLevel.PRIVATE)
    private String password;
}
