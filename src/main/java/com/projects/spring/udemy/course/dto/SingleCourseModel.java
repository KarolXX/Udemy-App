package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.course.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

// used when client will click on the course from menu
@NoArgsConstructor
@AllArgsConstructor
@Data
public class SingleCourseModel {
    private Course course;
    private boolean boughtCourse;
    private Double userRate;
    private boolean likedCourse;
    private int usersNumber;
}
