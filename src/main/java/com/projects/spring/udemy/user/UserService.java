package com.projects.spring.udemy.user;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository repository;
    private CourseRepository courseRepository;

    public UserService(UserRepository repository, CourseRepository courseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
    }

    Optional<User> createUser(User source) {
        Optional<User> existingUser = repository.findAll()
                .stream()
                .filter(user -> user.getName().equals(source.getName()))
                .findAny();
        if(existingUser.isPresent())
            return Optional.empty();
        else
            return Optional.of(repository.save(source));
    }

    public List<CourseInMenu> getUserFavouriteCourses(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
        List<Integer> likedCourseIDs = user.getLikedCourses()
                .stream().map(Course::getId)
                .collect(Collectors.toList());
        return courseRepository.getCourseMenuByIdIsIn(likedCourseIDs);
    }
}
