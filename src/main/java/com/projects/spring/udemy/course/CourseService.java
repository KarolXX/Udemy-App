package com.projects.spring.udemy.course;

import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.course.dto.CommentWithUserID;
import com.projects.spring.udemy.course.dto.CourseWithUserIDs;
import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.relationship.CourseRatingKey;
import com.projects.spring.udemy.relationship.CourseRatingRepository;
import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private CourseRepository repository;
    private UserRepository userRepository;
    private CourseRatingRepository ratingRepository;

    public CourseService(CourseRepository repository, UserRepository userRepository, CourseRatingRepository ratingRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
    }

    public CourseWithUserIDs getCourse(Integer id) {
        Course target = repository.findById(id).get();
                //.orElseThrow(() -> new IllegalArgumentException("No course with given id"));
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
}
