package com.projects.spring.udemy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.spring.udemy.file.ImageClass;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
abstract public class AppUserTemplate extends ImageClass {
    @NotBlank(message = "Add username!")
    private String name;
    @NotBlank(message = "Add password!")
    @JsonIgnore
    private String password;
    private String salt;

    /**
     * Author and User entities (subclasses) needs this no-argument constructor for their no-argument constructors
     */
    public AppUserTemplate() {
    }

    public AppUserTemplate(@NotBlank(message = "Add username!") String name, @NotBlank(message = "Add password!") String password, String salt) {
        this.name = name;
        this.password = password;
        this.salt = salt;
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
