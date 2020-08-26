package com.msca.youtubedlgui.backend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@ConfigurationProperties(prefix = "gallery")
@Validated
@Data
public class GalleryFolderConfiguration {

    private List<FolderConfiguration> folders;



}