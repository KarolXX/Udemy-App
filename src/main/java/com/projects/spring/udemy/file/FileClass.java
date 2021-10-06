package com.projects.spring.udemy.file;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class FileClass {
    @OneToOne
    @JoinColumn(name = "image_id")
    private AppFile file;

    public AppFile getFile() {
        return file;
    }

    public void setFile(AppFile file) {
        this.file = file;
    }
}
