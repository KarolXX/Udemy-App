package com.projects.spring.udemy.course;

import com.projects.spring.udemy.category.Category;
import com.projects.spring.udemy.relationship.CourseRating;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int courseId;
    @NotBlank(message = "Add the course's title!")
    private String title;
    private String description;
    @NotNull
    private int price;
    private int promotion;

    @ManyToMany(mappedBy = "courses")
    private Set<Category> categories;

    @OneToMany(mappedBy = "course")
    private Set<CourseRating> ratings;

    public Course() {
    }

    public int getId() {
        return courseId;
    }

    public void setId(int id) {
        this.courseId = id;
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

    Set<Category> getCategories() {
        return categories;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    Set<CourseRating> getRatings() {
        return ratings;
    }

    public void setRatings(Set<CourseRating> ratings) {
        this.ratings = ratings;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPromotion() {
        return promotion;
    }

    public void setPromotion(int promotion) {
        this.promotion = promotion;
    }
}
