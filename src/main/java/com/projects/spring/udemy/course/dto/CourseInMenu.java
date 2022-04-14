package com.projects.spring.udemy.course.dto;

import com.projects.spring.udemy.category.Category;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

// DTO for displaying courses in menu.
// This is important DTO because with the course entity we fetch EAGERLY a few sets that we do not need in the menu
public class CourseInMenu {
    private int id;
    private String title;
    private Double averageRating;
    private Integer usersNumber;
    private int price;
    private Integer promotion;
    private String image;
    private Double sequence;

    public CourseInMenu(int id, String title, Double averageRating, Integer usersNumber, int price, Integer promotion, String image, Double sequence) {
        this.id = id;
        this.title = title;
        this.averageRating = averageRating;
        this.usersNumber = usersNumber;
        this.price = price;
        this.promotion = promotion;
        this.image = image;
        this.sequence = sequence;
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

    public Integer getUsersNumber() {
        return usersNumber;
    }

    void setUsersNumber(Integer usersNumber) {
        this.usersNumber = usersNumber;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }
}
