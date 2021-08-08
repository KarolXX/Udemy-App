package com.projects.spring.udemy.course;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.course.dto.CourseWithUserIDs;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private CourseRepository repository;
    private CourseService service;

    public CourseController(CourseRepository repository, CourseService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    ResponseEntity<List<CourseInMenu>> getAllCourses() {
        logger.warn("Exposing all the courses!");
        var result = repository.getCourseMenu();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    ResponseEntity<CourseWithUserIDs> getCourseById(@PathVariable Integer id) {
        logger.warn("Exposing course");
        var result = service.getCourse(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    ResponseEntity<Course> createCourse(@RequestBody Course source) {
        logger.info("New course has been created");
        var result = repository.save(source);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(source);
    }

    @PostMapping("/course-buying")
    ResponseEntity<?> buyCourse(@RequestBody CourseRatingKey key) {
        logger.warn("Client bought course");
        var result = service.buyCourse(key);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{courseId}")
    ResponseEntity<?> deleteCourse(@PathVariable Integer courseId) {
        logger.warn("Course has been deleted");
        repository.deleteById(courseId);
        return ResponseEntity.noContent().build();
    }

}
