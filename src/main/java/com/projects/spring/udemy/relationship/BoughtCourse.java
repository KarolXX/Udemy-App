package com.projects.spring.udemy.relationship;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.projects.spring.udemy.course.Course;
import com.projects.spring.udemy.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "bought_courses")
@NoArgsConstructor
@Data
public class BoughtCourse {
    @EmbeddedId
    private BoughtCourseKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    private Double rating = null;

    public BoughtCourse(BoughtCourseKey key) {
        this.id = key;
    }

}
