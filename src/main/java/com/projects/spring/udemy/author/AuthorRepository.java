package com.projects.spring.udemy.author;

import org.keycloak.adapters.jaas.AbstractKeycloakLoginModule;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {
    Boolean existsByName(String name);

    Boolean existsBySalt(String name);

    Optional<Author> findById(Integer id);

    Optional<Author> findByName(String name);

    Optional<Author> findAuthorCourseByCourseId(@Param("id") Integer courseId);
    
    List<Author> findAll();

    Author save(Author author);
}
