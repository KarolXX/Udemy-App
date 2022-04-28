package com.projects.spring.udemy.user;

import com.projects.spring.udemy.AppUserTemplate;
import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.relationship.BoughtCourse;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends AppUserTemplate {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int userId;
    private int budget;

    @OneToMany(mappedBy = "user")
    private Set<BoughtCourse> courses;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "course_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> likedCourses;

    /**
     * Hibernate needs no-argument constructor
     */
    public User() {
    }

    public User(@NotBlank(message = "Add username!") String name, @NotBlank(message = "Add password!") String password, String salt) {
        super(name, password, salt);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    Set<BoughtCourse> getCourses() {
        return courses;
    }

    public void setCourses(Set<BoughtCourse> ratings) {
        this.courses = ratings;
    }

    Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    Set<Course> getLikedCourses() {
        return likedCourses;
    }

    public void setLikedCourses(Set<Course> likedCourses) {
        this.likedCourses = likedCourses;
    }

    // methods for keeping in-sync both sides of bidirectional association
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setUser(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setUser(null);
    }
}