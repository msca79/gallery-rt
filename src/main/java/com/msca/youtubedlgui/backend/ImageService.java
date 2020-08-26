package com.msca.youtubedlgui.backend;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class ImageService {

    public static final int SERVLET_CONTEXT_INDEX = 0;
    public static final int VIRTUAL_FOLDER_INDEX = 1;
    public static final int PATH_INDEX = 2;
    @Autowired
    private GalleryFolderConfiguration config;

    @Value("${gallery.cache-folder}")
    private String cacheFolder;

    @Value("${gallery.cache-enabled:true}")
    private boolean cacheEnabled;

    protected static String[] split(String requestPath /* /image/gallery1/parlament/20190921_094120.jpg */) {
        String[] split = new String[3];
        requestPath = requestPath.trim();
        int firstSlash = requestPath.indexOf("/", 1);
        if (firstSlash == -1) {
            return null;
        }
        split[SERVLET_CONTEXT_INDEX] = requestPath.substring(1, firstSlash);//servlet context

        int secondSlash = requestPath.indexOf("/", firstSlash + 1);
        if (secondSlash == -1) {
            return null;
        }
        split[VIRTUAL_FOLDER_INDEX] = requestPath.substring(firstSlash + 1, secondSlash);//virtualfolder


        if (requestPath.length() <= secondSlash + 1) {
            return null;
        }
        split[PATH_INDEX] = requestPath.substring(secondSlash + 1);//path
        return split;
    }

    @RequestMapping(value = "/image/**", method = RequestMethod.GET,
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getImage(
            HttpServletRequest request,
            @RequestParam(value = "size", required = false) Integer size
    ) throws IOException {

        String requestPath = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String[] splitted = split(requestPath);
        String virtualFolder = splitted[VIRTUAL_FOLDER_INDEX];
        String path = splitted[PATH_INDEX];

        FolderConfiguration folderConfig = findFolderConfig(virtualFolder);

        String realFullPath = folderConfig.getRealFolder() + "/" + path;

        File realFile = new File(realFullPath);
        if (!realFile.exists()) {
            //TODO: clear cache
            return ResponseEntity.notFound().build();
        }

        String mimeType = Files.probeContentType(realFile.toPath());

        File fileToRespond;
        if (Boolean.TRUE.equals(folderConfig.getCache()) && size != null) {
            fileToRespond = getFromCache(virtualFolder, size, path, realFile);
        } else {
            fileToRespond = realFile;
        }

        //TODO: stream if video type
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(mimeType))
                .body(new InputStreamResource(new FileInputStream(fileToRespond)));
    }

    private File getFromCache(String virtualFolder, Integer size, String path, File realFile) throws IOException {
        String cachePath = cacheFolder + "/" + size + "/" + virtualFolder + "/" + path;
        File cacheFile = new File(cachePath);
        if (!cacheFile.exists()) {
            new File(FilenameUtils.getFullPath(cachePath)).mkdirs();
            Thumbnails.of(realFile)
                    .size(size, size)
                    .toFile(cacheFile);
        }
        return cacheFile;
    }

    private FolderConfiguration findFolderConfig(String virtualName) {
        for (FolderConfiguration f : config.getFolders()) {
            System.out.println("??" + f.getVirtualName());
            if (f.getVirtualName() != null &&
                    f.getVirtualName().equalsIgnoreCase(virtualName)) {
                return f;
            }
        }
        throw new RuntimeException("Virtual Folder not found " + virtualName);
    }


//       System.out.println("ext: " + FilenameUtils.getExtension(fullPath));//jpg
//        System.out.println("base: " + FilenameUtils.getBaseName(fullPath));//20190921_094120
//        System.out.println("full: " + FilenameUtils.getFullPath(fullPath));// d:/gallery/parlament/
//        System.out.println("name: " + FilenameUtils.getName(fullPath));//20190921_094120.jpg
//        System.out.println("requestPath: " + FilenameUtils.getPath(fullPath));//gallery/parlament/

}
