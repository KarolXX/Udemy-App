package com.projects.spring.udemy.file;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public class ImageClass {
    @OneToOne
    @JoinColumn(name = "image_id")
    private AppImage image;

    public AppImage getImage() {
        return image;
    }

    public void setImage(AppImage image) {
        this.image = image;
    }
}
