package com.chess.chessfx.menu;

import java.io.*;
import java.util.Properties;

/**
 * I wanted settings for app configuration:
 * Learning Resources:
 * <a href="https://stackoverflow.com/questions/4580005/what-is-the-simplest-way-to-do-settings-files-in-java">...</a>
 * https://www.baeldung.com/java-properties
 * <a href="https://www.geeksforgeeks.org/java/java-util-properties-class-java/">...</a>
 *
 */
public class PropertiesManager {
    // Config file should be convenient and hidden from user.
    // This is quite primitive.
    String PROPERTIES_FILE_PATH = System.getProperty("user.home") + "/.chessFXAppData";

    Properties properties;

    public PropertiesManager() {
        properties = new Properties();
        try {
            FileInputStream fileInputStream = new FileInputStream(PROPERTIES_FILE_PATH);
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("First properties check failed.\nPath was " + PROPERTIES_FILE_PATH);
            createDefaultPropertiesFile();
        }
    }

    private void createDefaultPropertiesFile() {
        // Defaults
        properties.setProperty("theme", "light-theme.css");

        // Try to save the properties to the file
        try {
            properties.store(new FileOutputStream(PROPERTIES_FILE_PATH), null);
            System.out.println("Successfully wrote default properties to: " + PROPERTIES_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error that should never happen.\nPath to file was: "
                    + PROPERTIES_FILE_PATH);
        }
    }

    public String getThemeFileName() {
        return properties.getProperty("theme");
    }
}
