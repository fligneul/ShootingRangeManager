package com.fligneul.srm.util;

import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.Optional;

public class FileUtil {
    private static final Logger LOGGER = LogManager.getLogger(FileUtil.class);

    public static <T> Optional<Image> loadImage(final String path) {
        try {
            FileInputStream file = new FileInputStream(path);
            return Optional.of(new Image(file));
        } catch (FileNotFoundException e) {
            LOGGER.error("Error during image loading", e);
            return Optional.empty();
        }
    }

    public static <T> Optional<Image> loadImage(final Path path) {
        try {
            FileInputStream file = new FileInputStream(path.toFile());
            return Optional.of(new Image(file));
        } catch (FileNotFoundException e) {
            LOGGER.error("Error during image loading", e);
            return Optional.empty();
        }
    }
}
