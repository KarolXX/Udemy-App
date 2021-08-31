package com.projects.spring.udemy.user;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    User createUser(User source) {
        Optional<User> existingUser = repository.findAll()
                .stream()
                .filter(user -> user.getName().equals(source.getName()))
                .findAny();
        if(existingUser.isPresent())
            throw new IllegalArgumentException("User with such nick already exists");
        else
            return repository.save(source);
    }

    public List<CourseInMenu> getUserFavouriteCourses(Integer id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No user with given id"));
        return new ArrayList(user.getLikedCourses());
    }
}
