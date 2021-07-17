package com.projects.spring.udemy.category.dto;

import com.projects.spring.udemy.category.Category;
import com.projects.spring.udemy.course.Course;

import java.util.ArrayList;
import java.util.List;

public class CategoryAndCourses {
    private Category category;
    private List<Course> courses;

    public CategoryAndCourses(Category category, List<Course> courses) {
        this.category = category;
        this.courses = courses;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
