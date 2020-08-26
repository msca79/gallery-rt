package com.msca.youtubedlgui.backend;

import lombok.Data;

@Data
public  class FolderConfiguration {

    private String realFolder;
    private String virtualName;
    private Boolean cache;
}
