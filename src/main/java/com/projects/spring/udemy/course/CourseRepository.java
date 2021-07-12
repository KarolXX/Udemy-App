package com.projects.spring.udemy.course;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.title, AVG(cr.rating), COUNT(u.userId)) " +
            "FROM Course c " +
            "JOIN CourseRating cr ON c.courseId = cr.id.courseId " +
            "JOIN User u ON u.userId = cr.id.userId " +
            "GROUP BY c.courseId")
    List<CourseInMenu> getCourseMenu();
}
