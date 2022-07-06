package com.projects.spring.udemy.adapter;

import com.projects.spring.udemy.author.Author;
import com.projects.spring.udemy.author.AuthorRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface AuthorRepositoryBean extends AuthorRepository, JpaRepository<Author, Integer> {
    @Query(
            nativeQuery = true,
            value = "SELECT a.* FROM author_course ac " +
                    "JOIN authors a ON ac.author_id = a.author_id " +
                    "WHERE ac.course_id = :id")
    Optional<Author> findAuthorCourseByCourseId(@Param("id") Integer courseId);
}
