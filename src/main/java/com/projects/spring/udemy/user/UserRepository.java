package com.projects.spring.udemy.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String string);

    @Query(
            nativeQuery = true,
            value = "INSERT INTO course_likes (course_id, user_id) VALUES (:courseId, :userId)"
    )
    void likeCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);

    @Query(
            nativeQuery = true,
            value = "DELETE FROM course_likes WHERE course_id = :courseId AND user_id = :userId"
    )
    void dislikeCourse(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}
