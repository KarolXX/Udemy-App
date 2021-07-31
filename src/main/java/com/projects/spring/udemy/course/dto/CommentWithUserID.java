package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.comment.Comment;

public class CommentWithUserID {
    private Comment comment;
    private Integer userId;

    public CommentWithUserID(Comment comment, Integer userId) {
        this.comment = comment;
        this.userId = userId;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
