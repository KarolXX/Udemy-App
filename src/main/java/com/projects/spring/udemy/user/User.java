package com.projects.spring.udemy.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.spring.udemy.AppUserTemplate;
import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.relationship.BoughtCourse;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@SuperBuilder
@Data
public class User extends AppUserTemplate {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    @Setter(AccessLevel.PRIVATE)
    private int userId;
    private int budget;

    @OneToMany(mappedBy = "user")
    @Setter(AccessLevel.PACKAGE)
    @JsonIgnore
    private Set<BoughtCourse> courses;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    @JsonIgnore
    private Set<Comment> comments;

    @ManyToMany
    @JoinTable(
            name = "course_likes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @JsonIgnore
    private Set<Course> likedCourses;

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