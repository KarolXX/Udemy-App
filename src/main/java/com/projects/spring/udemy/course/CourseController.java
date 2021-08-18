package com.projects.spring.udemy.course;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.course.dto.CourseWithUserIDs;
import com.projects.spring.udemy.course.dto.UploadDto;
import com.projects.spring.udemy.relationship.CourseRatingKey;
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

    @GetMapping(path = "/{id}", params = "category")
    ResponseEntity<List<CourseInMenu>> getCoursesByCategoryId(
            @PathVariable Integer id,
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

    @DeleteMapping("/{courseId}")
    ResponseEntity<?> deleteCourse(@PathVariable Integer courseId) {
        logger.warn("Course has been deleted");
        repository.deleteById(courseId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping(value = "/img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<?> uploadFIle(UploadDto uploadDto) {
        String folderPath = configuration.getPath();
        File folder = new File(folderPath);
        File file = null;

        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            file = new File(folderPath, "321.png");
            //file.createNewFile();

            uploadDto.getFile().transferTo(file);
        } catch (IOException ex) {

        }

        if (file == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.ok(file.getAbsolutePath());
        }
    }

    //    @GetMapping(value = "/img")
//    public ResponseEntity<?> getFile() {
//        //String folderPath = "C:\\Users\\karol\\Desktop\\JAVA\\udemy\\upload";
//        String folderPath = configuration.getPath();
//        File file = new File(folderPath,"321.png");
//
//        if(file.exists()) {
//            try {
//                InputStreamResource isr = new InputStreamResource(
//                        new FileInputStream(file)
//                );
//
//                HttpHeaders headers = new HttpHeaders();
//                headers.add("Content-Disposition", "attachment; filename=\"123.png\"");
//
//                return ResponseEntity.ok()
//                        .headers(headers)
//                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                        .body(isr);
//            } catch(IOException ex) {
//               // return ResponseEntity.notFound().build();
//            }
//        }
//
//        return ResponseEntity.badRequest().body("sss");
//    }

    @GetMapping(value = "img")
    ResponseEntity<?> getCourseImage() {
        logger.warn("Exposing course image");
        File folder = new File(configuration.getPath());
        File file = new File(folder, "img.png");

        if(!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        try {
            InputStreamResource isr = new InputStreamResource(
                    new FileInputStream(file)
            );
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=\"xxx.png\"");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(isr);
        } catch (FileNotFoundException e) {

        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
