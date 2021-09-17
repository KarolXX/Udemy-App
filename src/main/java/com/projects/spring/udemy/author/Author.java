package com.projects.spring.udemy.author;

import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.file.AppImage;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int authorId;
    @NotBlank(message = "Author needs name")
    private String name;
    private String occupation;
    @NotBlank(message = "Author must have a description")
    private String description;
    private Double averageRating;

//    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinTable(
//
//    )
//    private Set<Course> courses;

    @OneToOne
    @JoinColumn(name = "image_id")
    private AppImage image;

    public Author() {
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public AppImage getImage() {
        return image;
    }

    public void setImage(AppImage image) {
        this.image = image;
    }
}
