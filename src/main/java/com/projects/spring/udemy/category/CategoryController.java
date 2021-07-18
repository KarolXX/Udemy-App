package com.projects.spring.udemy.category;

import com.projects.spring.udemy.category.dto.CategoryAndCourses;
import com.projects.spring.udemy.course.Course;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private CategoryRepository repository;
    private CategoryService service;

    public CategoryController(CategoryRepository repository, CategoryService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<Category>> getAllCategories() {
        logger.warn("Exposing all the categories!");
        var result = repository.findAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<CategoryAndCourses> getCategoryCourses(@PathVariable int id) {
        logger.warn("Exposing category courses");
        var result = service.getCourses(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity createCategory(@RequestBody Category source) {
        logger.info("New category has been created");
        var result = repository.save(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }
}