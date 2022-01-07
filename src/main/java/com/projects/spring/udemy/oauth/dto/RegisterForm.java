package com.projects.spring.udemy.oauth.dto;

public class RegisterForm {
    private String name;
    private String password;

    public RegisterForm() {
    }

    public RegisterForm(String name, String password) {
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

    public void setPassword(String password) {
        this.password = password;
    }
}
