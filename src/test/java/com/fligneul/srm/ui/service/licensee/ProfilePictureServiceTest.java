package com.fligneul.srm.ui.service.licensee;

import javafx.scene.image.Image;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(ApplicationExtension.class)
public class ProfilePictureServiceTest {
    private final String salt = RandomStringUtils.randomAlphanumeric(16).toUpperCase();

    @AfterEach
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void cleanUp() {
        File tmpDirectory = Path.of(System.getenv("TMP"), "ShootingRangeManager_" + salt).toFile();
        for (File file : Objects.requireNonNull(tmpDirectory.listFiles())) {
            file.delete();
        }
        tmpDirectory.delete();
    }

    @Test
    void profilePictureTest() throws URISyntaxException {
        String defaultPicturePath = "/com/fligneul/srm/image/default-picture.png";
        Path pictureDirectory = Path.of(System.getenv("TMP"), "ShootingRangeManager_" + salt);
        ProfilePictureService profilePictureService = new ProfilePictureService(defaultPicturePath, pictureDirectory);

        // Get default picture
        Optional<Image> defaultPicture = profilePictureService.getProfilePicture();
        Assertions.assertTrue(defaultPicture.isPresent());

        // Try to get non-existing picture, it should be the default one
        Assertions.assertEquals(defaultPicture, profilePictureService.getProfilePicture("wrong-picture.png"));

        // Save picture
        Optional<String> savedFileName = profilePictureService.saveProfilePicture(Path.of(getClass().getResource("/com/fligneul/srm/image/default-picture.png").toURI()), Optional.empty());
        Assertions.assertTrue(savedFileName.isPresent());
        Assertions.assertTrue(Files.exists(Path.of(pictureDirectory.toString(), savedFileName.get())));

        // Test retrieve saved picture
        Optional<Image> savedImage = profilePictureService.getProfilePicture(savedFileName.get());
        Assertions.assertTrue(savedImage.isPresent());
        // Saved picture should be different from the original picture
        Assertions.assertNotEquals(Optional.of(new Image(Path.of(getClass().getResource("/com/fligneul/srm/image/default-picture.png").toURI()).toUri().toString())), savedImage);
        // Saved picture should be different from the default one
        Assertions.assertNotEquals(profilePictureService.getProfilePicture(), savedImage);

        // Resave and delete old picture
        Optional<String> fileName2 = profilePictureService.saveProfilePicture(Path.of(getClass().getResource("/com/fligneul/srm/image/default-picture.png").toURI()), savedFileName);
        Assertions.assertTrue(fileName2.isPresent());
        Assertions.assertNotEquals(savedFileName, fileName2);
        Assertions.assertFalse(Files.exists(Path.of(pictureDirectory.toString(), savedFileName.get())));
        Assertions.assertTrue(Files.exists(Path.of(pictureDirectory.toString(), fileName2.get())));
    }
}