package com.projects.spring.udemy.course.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

// dto used for updating any attributes.
// Class of entity (Course) should not be used to any other things than actual entity that will/is be stored in db
@NoArgsConstructor
@Data
public class UpdatedCourse {
    private String title;
    private String description;
    private Integer price;
    private Integer promotion;
}
