package com.projects.spring.udemy.course;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private CourseRepository repository;

    public CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    ResponseEntity<List<CourseInMenu>> getAllCourses() {
        logger.warn("Exposing all the courses!");
        var result = repository.getCourseMenu();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<Course> getCourseById(@PathVariable Integer id) {
        logger.warn("Exposing course");
        var result = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<Course> createCourse(@RequestBody Course source) {
        logger.info("New course has been created");
        var result = repository.save(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(source);
    }

}
