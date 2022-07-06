package com.projects.spring.udemy.relationship;

import java.util.List;
import java.util.Optional;

public interface BoughtCourseRepository {
    Optional<BoughtCourse> findById(BoughtCourseKey id);
    BoughtCourse save(BoughtCourse bc);

    List<BoughtCourse> findBoughtCoursesById_UserIdIsIn(List<Integer> id);

    Boolean existsById_CourseIdAndId_UserId(Integer courseId, Integer userId);
}
