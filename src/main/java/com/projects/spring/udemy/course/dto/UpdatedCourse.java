package com.projects.spring.udemy.course.dto;

import java.util.Optional;

// dto used for updating any attributes.
// Class of entity (Course) should not be used to any other things than actual entity that will/is be stored in db
public class UpdatedCourse {
    private String title;
    private String description;
    private Integer price;
    private Integer promotion;

    public UpdatedCourse() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }
}
