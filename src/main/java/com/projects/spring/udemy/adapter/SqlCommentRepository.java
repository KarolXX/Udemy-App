package com.projects.spring.udemy.adapter;

import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.comment.CommentRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlCommentRepository extends CommentRepository, JpaRepository<Comment, Integer> {
}
