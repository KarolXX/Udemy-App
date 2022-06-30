package com.projects.spring.udemy.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.projects.spring.udemy.course.Course;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "categories")
@NoArgsConstructor
@Data
public class Category {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    @Setter(AccessLevel.PRIVATE)
    private int categoryId;
    @NotBlank(message = "category's name must be not empty!")
    @Setter(AccessLevel.PRIVATE)
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name="course_category",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @Setter(AccessLevel.PRIVATE)
    @JsonIgnore // to avoid StackOverflowError because course has fetch type eager for categories
    private Set<Course> courses;

}