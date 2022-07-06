package com.projects.spring.udemy.adapter;

import com.projects.spring.udemy.file.AppFile;
import com.projects.spring.udemy.file.AppFileRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface AppFileRepositoryBean extends AppFileRepository, JpaRepository<AppFile, Integer> {
}
