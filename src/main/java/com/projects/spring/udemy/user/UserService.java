package com.projects.spring.udemy.user;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.course.CourseRepository;
import com.projects.spring.udemy.course.dto.CourseInMenu;
import com.projects.spring.udemy.relationship.BoughtCourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository repository;
    private final CourseRepository courseRepository;
    private final BoughtCourseRepository boughtCourseRepository;

    public UserService(UserRepository repository, CourseRepository courseRepository, BoughtCourseRepository boughtCourseRepository) {
        this.repository = repository;
        this.courseRepository = courseRepository;
        this.boughtCourseRepository = boughtCourseRepository;
    }

    User findUserById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
    }

    void likeCourse(Integer userId, Integer courseId) {
        repository.likeCourse(userId, courseId);
    }

    List<CourseInMenu> getUserFavouriteCourses(Integer id) {
        User user = findUserById(id);

        // find the IDs of liked courses, pass to repo and fetch them by IDs
        List<Integer> likedCourseIDs = user.getLikedCourses()
                .stream().map(Course::getId)
                .collect(Collectors.toList());
        return courseRepository.getCourseMenuByIdIsIn(likedCourseIDs);
    }

    void dislikeCourse(Integer userId, Integer courseId) {
        repository.dislikeCourse(userId, courseId);
    }

    void deleteUser(Integer id) {
        if (repository.existsById(id))
            repository.deleteById(id);
    }
}
