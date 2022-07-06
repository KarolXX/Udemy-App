package com.projects.spring.udemy.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projects.spring.udemy.course.dto.CourseInMenu;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {
    Optional<Category> findById(@Param("id") Integer id);

    Category save(Category c);

    List<Category> findAll();
}
