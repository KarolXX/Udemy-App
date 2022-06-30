package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.category.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// DTO for displaying courses in menu.
// This is important DTO because with the course entity we fetch EAGERLY a few sets that we do not need in the menu
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseInMenu {
    private int id;
    private String title;
    private Double averageRating;
    private Integer usersNumber;
    private int price;
    private Integer promotion;
    private String image;
    private Double sequence;
}
