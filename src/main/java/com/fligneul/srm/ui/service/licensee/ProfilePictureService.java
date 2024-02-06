package com.fligneul.srm.ui.service.licensee;


import com.fligneul.srm.di.module.UIModule;
import javafx.scene.image.Image;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Named;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Service for profile picture
 */
public class ProfilePictureService {
    private static final Logger LOGGER = LogManager.getLogger(ProfilePictureService.class);
    private static final int PROFILE_PICTURE_TARGET_SIZE = 300;
    private static final String PROFILE_PICTURE_TARGET_EXTENSION = "png";
    private final String defaultPicturePath;
    private Image defaultPicture;
    private final Path pictureDirectoryPath;

    /**
     * Constructor
     */
    @Inject
    public ProfilePictureService(@Named(UIModule.DEFAULT_PICTURE_PATH_INJECT) final String defaultPicturePath,
                                 @Named(UIModule.PICTURE_DIRECTORY_PATH_INJECT) final Path pictureDirectoryPath) {
        this.defaultPicturePath = defaultPicturePath;
        this.pictureDirectoryPath = pictureDirectoryPath;

        // Check if directory exists
        File picturesDirectory = pictureDirectoryPath.toFile();
        if (!picturesDirectory.exists()) {
            LOGGER.warn("Pictures directory doesn't exists");
            if (picturesDirectory.mkdirs()) {
                LOGGER.info("Pictures directory successfully created");
            } else {
                LOGGER.error("Error during pictures directory creation");
            }
        }
    }

    /**
     * @return an Optional of the default profile picture
     */
    public Optional<Image> getProfilePicture() {
        return Optional.ofNullable(defaultPicture)
                .or(() -> {
                    try (InputStream in = getClass().getResourceAsStream(defaultPicturePath)) {
                        if (in != null) {
                            defaultPicture = new Image(in);
                            return Optional.of(defaultPicture);
                        }
                    } catch (IOException e) {
                        LOGGER.error("Can't find default picture", e);
                    }
                    return Optional.empty();
                });
    }

    /**
     * Return an Optional of the desired profile picture or the default one if path is not valid
     *
     * @param pictureFile
     *         path of the picture
     * @return an Optional of the profile picture
     */
    public Optional<Image> getProfilePicture(String pictureFile) {
        return Optional.ofNullable(pictureFile)
                .filter(Predicate.not(String::isBlank))
                .map(file -> pictureDirectoryPath.resolve(pictureFile))
                .filter(Files::exists)
                .map(path -> new Image(path.toString()))
                .or(this::getProfilePicture);
    }

    /**
     * Save the provided profile picture in the data folder
     * Picture profile is resized and a random filename is generated
     * If an already existing picture is provided, it will be deleted before save
     *
     * @param picture
     *         profile picture to save
     * @param alreadySavedPicture
     *         name of the already saved picture
     * @return the random filename generated for the saved picture
     */
    public Optional<String> saveProfilePicture(Path picture, Optional<String> alreadySavedPicture) {
        try {
            // Remove old picture if exist
            alreadySavedPicture
                    .filter(Predicate.not(String::isBlank))
                    .ifPresent(name -> {
                        File oldPicture = Path.of(pictureDirectoryPath.toString(), name).toFile();
                        if (oldPicture.delete()) {
                            LOGGER.info("Old profile picture successfully deleted");
                        } else {
                            LOGGER.error("Error during old profile picture deletion");
                        }
                    });
            // Load image and resize it
            BufferedImage inputImage = ImageIO.read(picture.toFile());
            BufferedImage scaledImage = Scalr.resize(inputImage, Scalr.Mode.FIT_TO_WIDTH, PROFILE_PICTURE_TARGET_SIZE);
            // Generate random filename
            String imageName = RandomStringUtils.randomAlphanumeric(32).toUpperCase() + "." + PROFILE_PICTURE_TARGET_EXTENSION;
            File outputFile = new File(pictureDirectoryPath.toString(), imageName);
            if (outputFile.exists()) {
                throw new IOException("Filename collision");
            }
            ImageIO.write(scaledImage, PROFILE_PICTURE_TARGET_EXTENSION, outputFile);
            return Optional.of(imageName);
        } catch (IOException e) {
            LOGGER.error("Error during picture save", e);
            return Optional.empty();
        }
    }
}
