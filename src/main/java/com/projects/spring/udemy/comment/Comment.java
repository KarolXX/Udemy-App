package com.projects.spring.udemy.comment;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int commentId;
    @NotBlank(message = "Your comment is empty!")
    private String text;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    public Comment() {
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
