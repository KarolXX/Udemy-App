package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.category.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CourseInMenu {
    private int id;
    private String title;
    private double averageRating;
    private long usersNumber;
    private int price;
    private Integer promotion;
    private String image;

    public CourseInMenu(int id, String title, double averageRating, long usersNumber, int price, Integer promotion, String image) {
        this.id = id;
        this.title = title;
        this.averageRating = averageRating;
        this.usersNumber = usersNumber;
        this.price = price;
        this.promotion = promotion;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public long getUsersNumber() {
        return usersNumber;
    }

    public void setUsersNumber(long usersNumber) {
        this.usersNumber = usersNumber;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
