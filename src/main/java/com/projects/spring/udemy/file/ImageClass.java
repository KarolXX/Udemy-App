package com.projects.spring.udemy.file;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class ImageClass {
    @OneToOne
    @JoinColumn(name = "image_id")
    private AppFile image;

    public AppFile getImage() {
        return image;
    }

    public void setImage(AppFile image) {
        this.image = image;
    }
}
