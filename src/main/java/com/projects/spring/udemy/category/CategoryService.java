package com.projects.spring.udemy.category;

import com.projects.spring.udemy.category.dto.CategoryAndCourses;
import com.projects.spring.udemy.course.Course;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public CategoryAndCourses getCourses(int id) {
        var category = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No category with given id"));
            var result = new CategoryAndCourses(category, new ArrayList<>(category.getCourses()));
        return result;
    }
}
