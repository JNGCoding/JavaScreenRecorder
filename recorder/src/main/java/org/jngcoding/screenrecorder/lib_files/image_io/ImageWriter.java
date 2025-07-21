package org.jngcoding.screenrecorder.lib_files.image_io;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageWriter {
    public static void write_image(File file, BufferedImage image) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        ImageIO.write(image, "png", file);
    }

    public static void write_image(File file, String format, BufferedImage image) throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
        ImageIO.write(image, format, file);
    }

    public static void write_image(String path, BufferedImage image) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        ImageIO.write(image, "png", file);
    }

    public static void write_image(String path, String format, BufferedImage image) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
        ImageIO.write(image, format, file);
    }
}