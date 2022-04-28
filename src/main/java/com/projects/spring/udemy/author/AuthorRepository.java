package com.projects.spring.udemy.author;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Integer> {
    Boolean existsByName(String name);
    Boolean existsBySalt(String name);
    Optional<Author> findByName(String name);

    @Query(
            nativeQuery = true,
            value = "SELECT a.* FROM author_course ac " +
                    "JOIN authors a ON ac.author_id = a.author_id " +
                    "WHERE ac.course_id = :id")
    Optional<Author> findCourseAuthorByCourseId(@Param("id") Integer courseId);
}
