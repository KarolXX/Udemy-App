package com.projects.spring.udemy.relationship;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRatingRepository extends JpaRepository<CourseRating, CourseRatingKey> {
    List<CourseRating> findCourseRatingsById_UserIdIsIn(List<Integer> id);
}
