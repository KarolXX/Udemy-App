package com.projects.spring.udemy.course;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Override
    @Query("SELECT course FROM Course course LEFT JOIN FETCH course.ratings WHERE course.courseId = :id")
    Optional<Course> findById(@Param("id") Integer id);

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.title, COALESCE ( AVG(cr.rating), 0 ), COUNT(u.userId)) " +
            "FROM Course c " +
            "LEFT  JOIN CourseRating cr ON c.courseId = cr.id.courseId " +
            "LEFT JOIN User u ON u.userId = cr.id.userId " +
            "GROUP BY c.title")
    List<CourseInMenu> getCourseMenu();
}
