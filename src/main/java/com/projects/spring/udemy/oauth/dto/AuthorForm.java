package com.projects.spring.udemy.oauth.dto;

import javax.validation.constraints.NotBlank;

public class AuthorForm extends UserForm{
    private String occupation;
    @NotBlank(message = "Author must have a description")
    private String description;

    public AuthorForm(String name, String password, String occupation, @NotBlank(message = "Author must have a description") String description) {
        super(name, password);
        this.occupation = occupation;
        this.description = description;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
