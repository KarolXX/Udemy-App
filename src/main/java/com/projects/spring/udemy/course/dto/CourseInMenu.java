package com.projects.spring.udemy.course.dto;

public class CourseInMenu {
    private String title;
    private double averageRating;
    private long usersNumber;

    public CourseInMenu(String title, double averageRating, long usersNumber) {
        this.title = title;
        this.averageRating = averageRating;
        this.usersNumber = usersNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public long getUsersNumber() {
        return usersNumber;
    }

    public void setUsersNumber(int usersNumber) {
        this.usersNumber = usersNumber;
    }
}
