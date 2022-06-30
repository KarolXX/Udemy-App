package com.projects.spring.udemy.author;

import com.projects.spring.udemy.AppUserTemplate;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.file.ImageClass;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@SuperBuilder
public class Author extends AppUserTemplate {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    @Setter(AccessLevel.PRIVATE)
    private int authorId;
    private String occupation;
    @NotBlank(message = "Author must have a description")
    @Setter(AccessLevel.PRIVATE)
    private String description;
    @Formula("( SELECT COALESCE( AVG(cr.rating), 0 ) FROM bought_courses cr " +
            "WHERE cr.course_id IN ( " +
            "SELECT c.course_id FROM author_course ac, courses c " +
            "WHERE ac.course_id = c.course_id AND ac.author_id = author_id " +
            ") )")
    private Double averageRating;
    private int budget;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "author_course",
            joinColumns = @JoinColumn(name = "author_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private Set<Course> courses;

}
