package com.projects.spring.udemy.relationship;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.user.User;

import javax.persistence.*;

@Entity
@Table(name = "course_ratings")
public class CourseRating {
    @EmbeddedId
    private CourseRatingKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    private Double rating = null;

    public CourseRating() {
    }

    public CourseRating(CourseRatingKey key) {
        this.id = key;
    }

    public CourseRatingKey getId() {
        return id;
    }

    public void setId(CourseRatingKey id) {
        this.id = id;
    }

    User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

//    @PostPersist
//    void prePersist() {
//
//    }
}
