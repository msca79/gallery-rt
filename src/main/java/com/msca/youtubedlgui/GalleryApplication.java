package com.msca.youtubedlgui;

import com.msca.youtubedlgui.backend.GalleryFolderConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties({GalleryFolderConfiguration.class})
public class GalleryApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(GalleryApplication.class, args);
    }

}

