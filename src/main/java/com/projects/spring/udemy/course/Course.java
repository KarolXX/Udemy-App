package com.projects.spring.udemy.course;

import com.projects.spring.udemy.category.Category;
import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.file.AppFile;
import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.relationship.CourseRating;
import com.projects.spring.udemy.user.User;
import org.hibernate.annotations.Formula;
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
    @Formula("( SELECT COALESCE( AVG(cr.rating), 0 ) FROM course_ratings cr WHERE cr.course_id = course_id )")
   // @Formula("SELECT AVG(cr.rating) FROM course_ratings cr WHERE cr.course_id = course_id")
    //@Transient
    private double averageRating;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CourseRating> ratings;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<Category> categories;

    @ManyToMany(mappedBy = "likedCourses")
    private Set<User> willingUsers;

    @OneToOne
    @JoinTable(
            name = "course_video",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private AppFile video;

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

    public Set<Category> getCategories() {
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

    public Integer getPromotion() {
        return promotion;
    }

    public void setPromotion(Integer promotion) {
        this.promotion = promotion;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    Set<User> getWillingUsers() {
        return willingUsers;
    }

    public void setWillingUsers(Set<User> willingUsers) {
        this.willingUsers = willingUsers;
    }

    public AppFile getVideo() {
        return video;
    }

    public void setVideo(AppFile video) {
        this.video = video;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setCourse(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setCourse(null);
    }
}
