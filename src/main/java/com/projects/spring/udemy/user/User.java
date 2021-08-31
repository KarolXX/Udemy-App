package com.projects.spring.udemy.user;

import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.relationship.CourseRating;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int userId;
    @NotBlank(message = "Add username!")
    private String name;

    @OneToMany(mappedBy = "user")
    private Set<CourseRating> ratings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "course_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> likedCourses;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    Set<CourseRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<CourseRating> ratings) {
        this.ratings = ratings;
    }

    Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    Set<Course> getLikedCourses() {
        return likedCourses;
    }

    public void setLikedCourses(Set<Course> likedCourses) {
        this.likedCourses = likedCourses;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setUser(null);
    }
}