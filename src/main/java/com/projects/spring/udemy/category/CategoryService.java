package com.projects.spring.udemy.category;

import com.projects.spring.udemy.course.dto.CourseInMenu;
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

    public List<CourseInMenu> getCourses(Integer id) {
         return repository.getCourseMenuByCategoryId(id);
    }
}
