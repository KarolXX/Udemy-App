package com.projects.spring.udemy.course;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.course.dto.CourseWithUserIDs;
import com.projects.spring.udemy.course.dto.UploadDto;
import com.projects.spring.udemy.course.dto.UserIDs;
import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import com.projects.spring.udemy.relationship.CourseRatingRepository;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/courses")
public class CourseController {
    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private CourseRepository repository;
    private CourseService service;
    private ConfigurationProperties configuration;

    public CourseController(CourseRepository repository, CourseService service, ConfigurationProperties configuration) {
        this.repository = repository;
        this.service = service;
        this.configuration = configuration;
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
//34 33 32 19 11 31 18 14 3 16
    @GetMapping("/{id}/participants/other-courses")
    ResponseEntity<List<CourseInMenu>> getOtherParticipantsCourses(@PathVariable("id") Integer targetCourseId) {
        logger.warn("Exposing users courses");
        var result = service.getOtherUsersCourses(targetCourseId);
        return ResponseEntity.ok(result);
    }

    @GetMapping(params = "category")
    ResponseEntity<List<CourseInMenu>> getCoursesByCategoryId(
            @RequestParam("category") Integer categoryId
    ) {
        logger.warn("Exposing course");
        var result = repository.getCourseMenuByCategoryId(categoryId);
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

    @PutMapping("/course-rating")
    ResponseEntity<?> rateCourse(@RequestBody CourseRating source) {
        logger.info("Client rated course");
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Access-Control-Allow-Origin", "*");
        double result = service.rateCourse(source);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{courseId}")
    ResponseEntity<?> deleteCourse(@PathVariable Integer courseId) {
        logger.warn("Course has been deleted");
        repository.deleteById(courseId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> uploadFIle(@PathVariable Integer id, UploadDto uploadDto) {
        logger.info("Uploading course image");
        String body = service.saveFile(id, uploadDto);
        return ResponseEntity.ok(body);
    }

    @GetMapping(value = "/{id}/img")
    ResponseEntity<?> getCourseImage(@PathVariable Integer id) {
        logger.warn("Exposing course image");
        return service.getFile(id);
    }

    @DeleteMapping(value = "/{id}/img")
    ResponseEntity<?> deleteCourseImage(@PathVariable Integer id) {
        logger.warn("Course image has been deleted!");
        service.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}
