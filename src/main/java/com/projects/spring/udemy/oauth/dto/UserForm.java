package com.projects.spring.udemy.oauth.dto;

import javax.validation.constraints.NotBlank;

public class UserForm {
    @NotBlank(message = "Please enter your name")
    private String name;
    @NotBlank(message = "Please enter your password")
    private String password;

    public UserForm() {
    }

    public UserForm(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }
}
