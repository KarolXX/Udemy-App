package com.projects.spring.udemy.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppImageRepository extends JpaRepository<AppImage, Integer> {
}
