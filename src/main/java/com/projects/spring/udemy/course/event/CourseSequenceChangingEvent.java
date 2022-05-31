package com.projects.spring.udemy.course.event;

// FIXME: rename to CourseSequenceChangedEvent as in course there is 'sequence' field, not 'order' because of problems with 'order' keyword in SQL
public class CourseSequenceChangingEvent {
    private int courseId;

    public CourseSequenceChangingEvent(int courseId) {
        this.courseId = courseId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
