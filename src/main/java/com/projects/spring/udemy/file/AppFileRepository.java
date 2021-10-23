package com.projects.spring.udemy.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AppFileRepository extends JpaRepository<AppFile, Integer> {
//    Query("INSERT INTO course_video(course_id, file_id) VALUES (")
//    void addCourseVideo();
}