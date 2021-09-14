package com.projects.spring.udemy.course;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.course.dto.SingleCourseModel;
import com.projects.spring.udemy.course.dto.UploadDto;
import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import com.projects.spring.udemy.relationship.CourseRatingRepository;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private CourseRepository repository;
    private UserRepository userRepository;
    private CourseRatingRepository ratingRepository;
    private ConfigurationProperties configuration;

    public CourseService(CourseRepository repository, UserRepository userRepository, CourseRatingRepository ratingRepository, ConfigurationProperties configuration) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.configuration = configuration;
    }

    public SingleCourseModel getCourse(Integer courseId, Integer userId) {
        Course target = repository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        Integer usersNumber = target.getRatings().size();

        boolean boughtCourse;
        Optional<Double> userRate;
        Optional<CourseRating> userRateExists = target.getRatings()
                .stream()
                .filter(rating -> rating.getId().getUserId() == userId)
                .findFirst();
        if(userRateExists.isEmpty()) {
            boughtCourse = false;
            userRate = null;
        }
        else {
            boughtCourse = true;
            userRate = userRateExists.stream()
                    .map(CourseRating::getRating)
                    .findFirst();
        }

        Optional<User> willingUser  = target.getWillingUsers()
                .stream().filter(user -> user.getUserId() == userId)
                .findFirst();
        boolean isCourseLiked = willingUser.isPresent();

        return new SingleCourseModel(target, boughtCourse, userRate, isCourseLiked, usersNumber);
    }

    public CourseRating buyCourse(CourseRatingKey key) {
        CourseRating association = new CourseRating(key);

        User user = userRepository.findById(key.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
        Course course = repository.findById(key.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        association.setCourse(course);
        association.setUser(user);

        return ratingRepository.save(association);
    }

    @Transactional
    public double rateCourse(CourseRating source) {
        CourseRating association = ratingRepository.findById(source.getId())
                .orElseThrow(() -> new IllegalArgumentException("This course or user is not available"));
        association.setRating(source.getRating());
        return association.getRating();
    }

    public List<CourseInMenu> getOtherParticipantsCourses(Integer targetCourseId, Integer userId) {
        Course targetCourse = repository.findById(targetCourseId)
                .orElseThrow(() -> new IllegalArgumentException("No ciurse with given id"));
        List<Integer> userIDs = targetCourse.getRatings()
                .stream().map(rate -> rate.getId().getUserId())
                .collect(Collectors.toList());
        List<CourseRating> source = ratingRepository.findCourseRatingsById_UserIdIsIn(userIDs);

        List<Integer> courseIDs = new ArrayList<>();
        source.stream().map(courseRating -> {
            Integer courseId = courseRating.getId().getCourseId();
            if(courseIDs.contains(courseId) || courseId.equals(targetCourseId))
                return null;
            else
                return courseId;
        }).forEach(courseId -> {
            if(courseId!= null)
                courseIDs.add(courseId);
        });

        return repository.getCourseMenuByIdIsIn(courseIDs);
    }

    @Transactional
    public String saveFile(Integer id, UploadDto uploadDto) {
        Course course = repository.getById(id);

        String folderPath = configuration.getImagesPath();
        File folder = new File(folderPath);
        File file = null;

        if (!folder.exists()) {
            folder.mkdir();
        }

        try {
            file = new File(generateUniqueFilename());
            uploadDto.getFile().transferTo(file);
        } catch (IOException ex) {

        }

        if (file == null) {
            return null;
        } else {
            course.setImage(file.getAbsolutePath());
            return file.getPath();
        }
    }

    ResponseEntity<?> getFile(Integer id) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        if(course.getImage() == null) {
            return ResponseEntity.badRequest().body("This course has no image");
        }

        File file = new File(course.getImage());

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

    @Transactional
    public void deleteFile(Integer id) {
        Course course = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));
        course.setImage(null);
    }

    private String generateUniqueFilename() {
        RandomString random = new RandomString(64);
        String filename = null;

        while(filename == null || new File(configuration.getImagesPath() + File.separator + filename).exists()) {
            filename = configuration.getImagesPath() + File.separator + random.nextString();
        }

        filename = filename + ".png";

        return filename;
    }
}
