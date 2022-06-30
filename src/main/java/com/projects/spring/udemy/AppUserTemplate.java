package com.projects.spring.udemy;

import com.projects.spring.udemy.file.ImageClass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@MappedSuperclass
@Data
@NoArgsConstructor
@SuperBuilder
abstract public class AppUserTemplate extends ImageClass {
    @NotBlank(message = "Add username!")
    private String name;
    @NotBlank(message = "Add password!")
    private String password;
    private String salt;
}
