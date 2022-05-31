package com.projects.spring.udemy.author;

import com.projects.spring.udemy.AppUserTemplate;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.file.ImageClass;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "authors")
public class Author extends AppUserTemplate {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int authorId;
    private String occupation;
    @NotBlank(message = "Author must have a description")
    private String description;
    //@Transient // to avoid "Unknown column 'average_rating' in 'field list'"
    //@Formula("( SELECT COALESCE(AVG(( SELECT COALESCE( AVG(cr.rating), 0 ) FROM course_ratings cr WHERE cr.course_id = c.course_id )), 0 ) FROM author_course ac JOIN courses c ON ac.course_id = c.course_id WHERE ac.author_id = 15 )")
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

    /**
     * Hibernate needs no-argument constructor
     */
    public Author() {
    }

    // FIXME: do I need @NotBlank for constructor parameters ?
    public Author(@NotBlank(message = "Author needs name") String name, @NotBlank(message = "Author needs password") String password, String salt, String occupation, @NotBlank(message = "Author must have a description") String description) {
        super(name, password, salt);
        this.occupation = occupation;
        this.description = description;
    }

    public int getAuthorId() {
        return authorId;
    }

    void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    void setDescription(String description) {
        this.description = description;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

}
