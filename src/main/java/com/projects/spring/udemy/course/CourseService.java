package com.projects.spring.udemy.course;

import com.projects.spring.udemy.ConfigurationProperties;
import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.course.dto.CommentWithUserID;
import com.projects.spring.udemy.course.dto.CourseWithUserIDs;
import com.projects.spring.udemy.course.dto.UploadDto;
import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import com.projects.spring.udemy.relationship.CourseRatingRepository;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collector;
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

    public CourseWithUserIDs getCourse(Integer id) {
        Course target = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));
        List<Integer> userIDs = new ArrayList<>(
                target.getRatings()
                    .stream().map(rating -> rating.getId().getUserId())
                    .collect(Collectors.toList())
        );
        return new CourseWithUserIDs(target, userIDs);
    }

    public CourseRating buyCourse(CourseRatingKey key) {
        CourseRating relationship = new CourseRating(key);

        User user = userRepository.findById(key.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
        Course course = repository.findById(key.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("No course with given id"));

        relationship.setCourse(course);
        relationship.setUser(user);

        return ratingRepository.save(relationship);
    }

    @Transactional
    public String saveFile(Integer id, UploadDto uploadDto) {
        Course course = repository.getById(id);

        String folderPath = configuration.getPath();
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
            return file.getAbsolutePath();
        }
    }

    private String generateUniqueFilename() {
        RandomString random = new RandomString(64);
        String filename = null;

        while(filename == null || new File(configuration.getPath() + File.separator + filename).exists()) {
            filename = configuration.getPath() + File.separator + random.nextString();
        }

        filename = filename + ".png";

        return filename;
    }
}
