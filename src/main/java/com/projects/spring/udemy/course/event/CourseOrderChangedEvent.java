package com.projects.spring.udemy.course.event;

// FIXME: rename to CourseSequenceChangedEvent as in course there is 'sequence' field, not 'order' because of problems with 'order' keyword in SQL
public class CourseOrderChangedEvent {
    private int courseId;

    public CourseOrderChangedEvent(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
