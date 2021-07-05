package com.projects.spring.udemy.course;

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
    ResponseEntity<List<Course>> getAllCourses() {
        logger.warn("Exposing all the courses!");
        var result = repository.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<Course> createCourse(@RequestBody Course source) {
        logger.info("New course has been created");
        var result = repository.save(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(source);
    }

}
