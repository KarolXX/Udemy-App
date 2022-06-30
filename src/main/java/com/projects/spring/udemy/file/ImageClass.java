package com.projects.spring.udemy.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
@SuperBuilder
@NoArgsConstructor
@Data
public class ImageClass {
    @OneToOne
    @JoinColumn(name = "image_id")
    private AppFile image;
}
