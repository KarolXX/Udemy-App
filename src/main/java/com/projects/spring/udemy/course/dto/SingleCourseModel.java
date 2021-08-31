package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.course.Course;

import java.util.Optional;

public class SingleCourseModel {
    private Course course;
    private Optional<Double> userRate;
    private boolean courseFavourite;
    private int usersNumber;

    public SingleCourseModel(
            Course course,
            Optional<Double> userRate,
            boolean courseFavourite,
            Integer usersNumber
    ) {
        this.course = course;
        this.userRate = userRate;
        this.courseFavourite = courseFavourite;
        this.usersNumber = usersNumber;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Optional<Double> getUserRate() {
        return userRate;
    }

    public void setUserRate(Optional<Double> userRate) {
        this.userRate = userRate;
    }

    public boolean isCourseFavourite() {
        return courseFavourite;
    }

    public void setCourseFavourite(boolean courseFavourite) {
        this.courseFavourite = courseFavourite;
    }

    public int getUsersNumber() {
        return usersNumber;
    }

    public void setUsersNumber(int usersNumber) {
        this.usersNumber = usersNumber;
    }
}
