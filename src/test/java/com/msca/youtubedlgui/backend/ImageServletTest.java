package com.msca.youtubedlgui.backend;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageServletTest {
    ImageService imageServlet = new ImageService();

    @Test
    public void testSplit1() {
        String[] splitted = imageServlet.split("/image/gallery1/parlament/20190921_094120.jpg");
        Assertions.assertEquals("image", splitted[0]);
        Assertions.assertEquals("gallery1", splitted[1]);
        Assertions.assertEquals("parlament/20190921_094120.jpg", splitted[2]);
    }

    @Test
    public void testSplit_onlyServlet() {
        String[] splitted = imageServlet.split("/image");
        Assertions.assertNull(splitted);
    }

    @Test
    public void testSplit_onlyFolder2() {
        String[] splitted = imageServlet.split("/image/gallery1");
        Assertions.assertNull(splitted);
    }

    @Test
    public void testSplit_onlyFolder() {
        String[] splitted = imageServlet.split("/image/gallery1/");
        Assertions.assertNull(splitted);
    }

    @Test
    public void testSplit_trim() {
        String[] splitted = imageServlet.split(" /image/gallery1/parlament/20190921_094120.jpg ");
        Assertions.assertEquals("image", splitted[0]);
        Assertions.assertEquals("gallery1", splitted[1]);
        Assertions.assertEquals("parlament/20190921_094120.jpg", splitted[2]);
    }
    @Test
    public void testSplit3() {
        String[] splitted = imageServlet.split(" /image/gallery1/a");
        Assertions.assertEquals("image", splitted[0]);
        Assertions.assertEquals("gallery1", splitted[1]);
        Assertions.assertEquals("a", splitted[2]);
    }
}