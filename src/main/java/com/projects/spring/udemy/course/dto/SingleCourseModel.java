package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.course.Course;

import java.util.ArrayList;
import java.util.List;

public class SingleCourseModel {
    private Course course;
    private List<Integer> userIDs;
    private int usersNumber;

    public SingleCourseModel(Course course, List<Integer> userIDs) {
        this.course = course;
        this.userIDs = userIDs;
        this.usersNumber = userIDs.size();
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<Integer> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<Integer> userIDs) {
        this.userIDs = userIDs;
    }

    public int getUsersNumber() {
        return usersNumber;
    }

    public void setUsersNumber(int usersNumber) {
        this.usersNumber = usersNumber;
    }
}
