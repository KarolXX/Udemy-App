package com.projects.spring.udemy.course;

import com.projects.spring.udemy.category.Category;
import com.projects.spring.udemy.comment.Comment;
import com.projects.spring.udemy.file.AppFile;
import com.projects.spring.udemy.file.ImageClass;
import com.projects.spring.udemy.relationship.BoughtCourse;
import com.projects.spring.udemy.user.User;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "courses")
@NoArgsConstructor
@Data
public class Course extends ImageClass {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int courseId;
    // model mapper needs public setters for title, description, price, promotion - especially for updateCourse() in CourseService
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

    @Setter(AccessLevel.PACKAGE)
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<BoughtCourse> ratings;

    @Setter(AccessLevel.PACKAGE)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course", fetch = FetchType.EAGER)
    private Set<Comment> comments;

    @Setter(AccessLevel.PACKAGE)
    @ManyToMany(mappedBy = "courses", fetch = FetchType.EAGER)
    private Set<Category> categories;

    @Setter(AccessLevel.PACKAGE)
    @ManyToMany(mappedBy = "likedCourses")
    private Set<User> willingUsers;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(
            name = "course_video",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "file_id")
    )
    private AppFile video;

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
