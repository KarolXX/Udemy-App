package com.projects.spring.udemy.course.dto;

import java.util.List;

public class UserIDs {
    private List<Integer> userIDs;

    public UserIDs() {
    }

    public List<Integer> getUserIDs() {
        return userIDs;
    }

    public void setUserIDs(List<Integer> userIDs) {
        this.userIDs = userIDs;
    }
}
