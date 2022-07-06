package com.projects.spring.udemy.file;

import java.util.Optional;

public interface AppFileRepository {
    Optional<AppFile> findById(Integer id);

    AppFile save(AppFile file);
}
