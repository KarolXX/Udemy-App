package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.course.Course;

import java.util.Optional;

public class SingleCourseModel {
    private Course course;
    private boolean boughtCourse;
    private Optional<Double> userRate;
    private boolean likedCourse;
    private int usersNumber;

    public SingleCourseModel(
            Course course,
            boolean boughtCourse,
            Optional<Double> userRate,
            boolean likedCourse,
            Integer usersNumber
    ) {
        this.course = course;
        this.boughtCourse = boughtCourse;
        this.userRate = userRate;
        this.likedCourse = likedCourse;
        this.usersNumber = usersNumber;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isBoughtCourse() {
        return boughtCourse;
    }

    public void setBoughtCourse(boolean boughtCourse) {
        this.boughtCourse = boughtCourse;
    }

    public Optional<Double> getUserRate() {
        return userRate;
    }

    public void setUserRate(Optional<Double> userRate) {
        this.userRate = userRate;
    }

    public boolean isLikedCourse() {
        return likedCourse;
    }

    public void setLikedCourse(boolean likedCourse) {
        this.likedCourse = likedCourse;
    }

    public int getUsersNumber() {
        return usersNumber;
    }

    public void setUsersNumber(int usersNumber) {
        this.usersNumber = usersNumber;
    }
}
