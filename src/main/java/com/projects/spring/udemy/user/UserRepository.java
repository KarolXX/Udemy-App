package com.projects.spring.udemy.user;

import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    boolean existsById(Integer id);

    boolean existsByName(String name);

    boolean existsBySalt(String name);

    Optional<User> findById(Integer id);

    Optional<User> findByName(String name);

    List<User> findAll();

    User save(User user);

    void deleteById(Integer id);

    void likeCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    void dislikeCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}
