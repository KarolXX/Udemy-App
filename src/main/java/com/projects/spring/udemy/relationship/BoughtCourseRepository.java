package com.projects.spring.udemy.relationship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoughtCourseRepository extends JpaRepository<BoughtCourse, BoughtCourseKey> {
    List<BoughtCourse> findBoughtCoursesById_UserIdIsIn(List<Integer> id);

    Boolean existsById_CourseIdAndId_UserId(Integer courseId, Integer userId);
}
