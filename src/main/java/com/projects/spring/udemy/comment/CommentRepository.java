package com.projects.spring.udemy.comment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(Integer integer);

    Comment save(Comment comment);

    void deleteById(Integer id);
}
