package com.projects.spring.udemy.category;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private CategoryRepository repository;

    public CategoryController(CategoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<Category>> getAllCategories() {
        logger.warn("Exposing all the categories!");
        var result = repository.findAll();
        return ResponseEntity.ok(result);
    }

//    @GetMapping("/{id}")
//    ResponseEntity<List<CourseInMenu>> getCategoryCourses(@PathVariable Integer id) {
//        logger.warn("Exposing category courses");
//        var result = repository.getCourseMenuByCategoryId(id);
//        return ResponseEntity.ok(result);
//    }

    @PostMapping
    ResponseEntity createCategory(@RequestBody Category source) {
        logger.info("New category has been created");
        var result = repository.save(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

//    @DeleteMapping("/{categoryId}")
//    ResponseEntity deleteCategory(@PathVariable Integer categoryId) {
//        logger.warn("Category has been deleted");
//
//    }
}
