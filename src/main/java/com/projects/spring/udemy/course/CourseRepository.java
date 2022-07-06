package com.projects.spring.udemy.course;

import com.projects.spring.udemy.course.dto.CourseInMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    Optional<Course> findById(@Param("id") Integer id);

    void updateCourseAverageRating(@Param("courseId") Integer courseId);

    List<CourseInMenu> getCourseMenu();

    Page<CourseInMenu> getCourseMenu(Pageable pageable);

    List<CourseInMenu> getCourseMenuByIdIsIn(@Param("ids") List<Integer> courseIDs);

    Course save(Course course);

    void deleteById(Integer id);
}
