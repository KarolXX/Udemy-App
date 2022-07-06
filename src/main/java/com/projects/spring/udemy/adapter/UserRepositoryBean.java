package com.projects.spring.udemy.adapter;

import com.projects.spring.udemy.user.User;
import com.projects.spring.udemy.user.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
interface UserRepositoryBean extends UserRepository, JpaRepository<User, Integer> {
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
