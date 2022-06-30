package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentWithUserID {
    @Valid
    private Comment comment;
    @NotNull
    private Integer userId;
}
