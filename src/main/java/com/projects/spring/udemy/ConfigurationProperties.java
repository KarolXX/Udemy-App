package com.projects.spring.udemy;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@org.springframework.boot.context.properties.ConfigurationProperties("properties")
public class ConfigurationProperties {
    private List<String> videoExtensions;
    private String imagesPath;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    public List<String> getVideoExtensions() {
        return videoExtensions;
    }

    public void setVideoExtensions(List<String> videoExtensions) {
        this.videoExtensions = videoExtensions;
    }

    public String getImagesPath() {
        return imagesPath;
    }

    public void setImagesPath(String imagesPath) {
        this.imagesPath = imagesPath;
    }
}
