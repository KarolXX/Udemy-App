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

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.courseId, c.title, COALESCE ( AVG(cr.rating), 0 ), COUNT(cr.user.userId), c.price, c.promotion, img.image) " +
            "FROM Course c " +
            "LEFT JOIN CourseRating cr ON c.courseId = cr.id.courseId " +
            "LEFT JOIN c.image img " +
            "GROUP BY c.title")
    List<CourseInMenu> getCourseMenu();

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.courseId, c.title, COALESCE ( AVG(r.rating), 0 ), COUNT(r.user.userId), c.price, c.promotion, img.image) " +
            "FROM Category cat " +
            "JOIN cat.courses c " +
            "LEFT JOIN c.ratings r " +
            "LEFT JOIN c.image img " +
            "WHERE cat.categoryId = :id " +
            "GROUP BY c.title"
    )
    List<CourseInMenu> getCourseMenuByCategoryId(@Param("id") Integer id);

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.courseId, c.title, COALESCE ( AVG(cr.rating), 0 ), COUNT(cr.user.userId), c.price, c.promotion, img.image) " +
            "FROM Course c " +
            "LEFT JOIN CourseRating cr ON c.courseId = cr.id.courseId " +
            "LEFT JOIN c.image img " +
            "WHERE c.courseId IN (:ids)" +
            "GROUP BY c.title")
    public List<CourseInMenu> getCourseMenuByIdIsIn(@Param("ids") List<Integer> courseIDs);
}
