package com.projects.spring.udemy.relationship;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class BoughtCourseKey implements Serializable {
    @Column(name = "user_id")
    private int userId;

    @Column(name = "course_id")
    private int courseId;

    public BoughtCourseKey() {
    }

    public BoughtCourseKey(Integer userId, Integer courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object object) {
        if(this == object)
            return true;
        if(!(object instanceof BoughtCourseKey))
            return false;
        BoughtCourseKey other = (BoughtCourseKey) object;
        return
                this.userId == other.userId && this.courseId == other.courseId;
    }

    @Override
    public final int hashCode() {
        int result = 31;
        result = 7 * result * userId;
        result = 7 * result * courseId;
        return result;
    }
}
