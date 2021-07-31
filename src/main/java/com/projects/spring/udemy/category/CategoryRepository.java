package com.projects.spring.udemy.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.projects.spring.udemy.course.dto.CourseInMenu;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Override
    @Query("SELECT cat FROM Category cat JOIN FETCH cat.courses WHERE cat.categoryId = :id")
    Optional<Category> findById(@Param("id") Integer id);

    @Query("SELECT new com.projects.spring.udemy.course.dto.CourseInMenu(c.title, COALESCE ( AVG(r.rating), 0 ), COUNT(r.user.userId)) " +
            "FROM Category cat " +
            "JOIN cat.courses c " +
            "LEFT JOIN c.ratings r " +
            "WHERE cat.categoryId = :id " +
            "GROUP BY c.title"
    )
    List<CourseInMenu> getCourseMenuByCategoryId(@Param("id") Integer id);
}
