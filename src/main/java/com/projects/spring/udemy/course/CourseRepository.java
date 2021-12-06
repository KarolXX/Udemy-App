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

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    @Override
    @Query("SELECT course FROM Course course LEFT JOIN FETCH course.ratings WHERE course.courseId = :id")
    Optional<Course> findById(@Param("id") Integer id);

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.courseId, c.title, COALESCE ( AVG(cr.rating), 0 ), COUNT(cr.user.userId), c.price, c.promotion, image.filePath) " +
            "FROM Course c " +
            "LEFT JOIN CourseRating cr ON c.courseId = cr.id.courseId " +
            "LEFT JOIN c.image image " +
            "GROUP BY c.title")
    List<CourseInMenu> getCourseMenu();

    @Modifying
    @Query("UPDATE Course c set c.averageRating = ( SELECT COALESCE( AVG(cr.rating), 0 ) FROM CourseRating cr WHERE cr.id.courseId = :courseId ) WHERE c.courseId = :courseId")
    void updateCourseAverageRating(@Param("courseId") Integer courseId);

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.courseId, c.title, c.averageRating, COUNT(cr.user.userId), c.price, c.promotion, image.filePath) " +
            "FROM Course c " +
            "LEFT JOIN CourseRating cr ON c.courseId = cr.id.courseId " +
            "LEFT JOIN c.image image " +
            "GROUP BY c.title"
    )
    Page<CourseInMenu> getCourseMenu(Pageable pageable);

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.courseId, c.title, COALESCE ( AVG(r.rating), 0 ), COUNT(r.user.userId), c.price, c.promotion, image.filePath) " +
            "FROM Category cat " +
            "JOIN cat.courses c " +
            "LEFT JOIN c.ratings r " +
            "LEFT JOIN c.image image " +
            "WHERE cat.categoryId = :id " +
            "GROUP BY c.title"
    )
    List<CourseInMenu> getCourseMenuByCategoryId(@Param("id") Integer id);

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.courseId, c.title, COALESCE ( AVG(cr.rating), 0 ), COUNT(cr.user.userId), c.price, c.promotion, image.filePath) " +
            "FROM Course c " +
            "LEFT JOIN CourseRating cr ON c.courseId = cr.id.courseId " +
            "LEFT JOIN c.image image " +
            "WHERE c.courseId IN (:ids)" +
            "GROUP BY c.title")
    public List<CourseInMenu> getCourseMenuByIdIsIn(@Param("ids") List<Integer> courseIDs);
}
