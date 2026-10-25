package com.chessfx.chessconfig;

import com.chessfx.log.Log;

import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static com.chessfx.ui.menu.ChessApp.THEME_DIR;

/**
 * I wanted settings for app configuration:
 * Learning Resources:
 * <a href="https://stackoverflow.com/questions/4580005/what-is-the-simplest-way-to-do-settings-files-in-java">...</a>
 * https://www.baeldung.com/java-properties
 * <a href="https://www.geeksforgeeks.org/java/java-util-properties-class-java/">...</a>
 *
 */
public class AppSettings {
    // TODO: MOVE this to chessconfig dir?

    // Config file should be convenient and hidden from user.
    // This is quite primitive.
    String PROPERTIES_FILE_PATH = System.getProperty("user.home") + "/.chessFXAppData/settings.properties";
    Properties properties;

    List<String> sceneStyleSheets;
    List<String> themes = new ArrayList<>(List.of("light", "dark", "wood", "chess", "ai-dark"));

    public AppSettings() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE_PATH);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Log.warn("First properties check failed.\nPath was " + PROPERTIES_FILE_PATH);
            createDefaultPropertiesFile();
        }
    }

    private void createDefaultPropertiesFile() {
        // Defaults
        properties.setProperty("theme", "light");
        writePropertiesToFile();
    }

    private void writePropertiesToFile() {
        // Try to save the properties to the file
        Path path = Paths.get(PROPERTIES_FILE_PATH);

        try {

            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            properties.store(new FileOutputStream(PROPERTIES_FILE_PATH), null);
            Log.info("Successfully wrote properties to: " + PROPERTIES_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Log.error("AppSettings", "Error that should never happen.\nPath to file was: "
                    + PROPERTIES_FILE_PATH);
        }
    }

    public String getThemeName() {
        return properties.getProperty("theme");
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setTheme(String theme) {
        properties.setProperty("theme", theme);
        writePropertiesToFile();

        sceneStyleSheets.addFirst(Objects.requireNonNull(
                        getClass().getResource(THEME_DIR + theme + ".css"))
                .toExternalForm());
        sceneStyleSheets.remove(1);
    }

    public void setSceneStyleSheets(ObservableList<String> sceneStyleSheets) {
        this.sceneStyleSheets = sceneStyleSheets;
    }
}
