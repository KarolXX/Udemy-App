package com.projects.spring.udemy.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Override
    @Query("SELECT cat FROM Category cat JOIN FETCH cat.courses WHERE cat.categoryId = :id")
    Optional<Category> findById(@Param("id") Integer id);
}
