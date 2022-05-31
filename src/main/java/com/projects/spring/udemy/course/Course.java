package com.projects.spring.udemy.course;

import com.projects.spring.udemy.category.Category;
import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.file.AppFile;
import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.relationship.BoughtCourse;
import com.projects.spring.udemy.user.User;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "courses")
public class Course extends ImageClass {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int courseId;
    @NotBlank(message = "Add the course's title!")
    private String title;
    private String description;
    private int price;
    private Integer promotion;
    private Double averageRating;
    private Integer usersNumber;
    // this field determines the order in which the course is displayed.
    // The higher the value, the faster it is displayed to the user
    private Double sequence;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<BoughtCourse> ratings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<Category> categories;

    @ManyToMany(mappedBy = "likedCourses")
    private Set<User> willingUsers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_video",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private AppFile video;

    public Course() {
    }

    // model mapper needs public setters for title, description, price, promotion - especially for updateCourse() in CourseService

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

    public Set<Category> getCategories() {
        return categories;
    }

    void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    Set<BoughtCourse> getRatings() {
        return ratings;
    }

    void setRatings(Set<BoughtCourse> ratings) {
        this.ratings = ratings;
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

    public Set<Comment> getComments() {
        return comments;
    }

    void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    Set<User> getWillingUsers() {
        return willingUsers;
    }

    void setWillingUsers(Set<User> willingUsers) {
        this.willingUsers = willingUsers;
    }

    public AppFile getVideo() {
        return video;
    }

    public void setVideo(AppFile video) {
        this.video = video;
    }

    public Integer getUsersNumber() {
        return usersNumber;
    }

    public void setUsersNumber(Integer usersNumber) {
        this.usersNumber = usersNumber;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Double getSequence() {
        return sequence;
    }

    public void setSequence(Double sequence) {
        this.sequence = sequence;
    }

    // methods for keeping in-sync both sides of bidirectional association
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setCourse(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setCourse(null);
    }
}
