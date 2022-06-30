package com.projects.spring.udemy.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "app_files")
@NoArgsConstructor
@Data
public class AppFile {
    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private int fileId;
    private String filePath;
    private String extension;

    public AppFile(String filePath) {
        this.filePath = filePath;
    }

}
