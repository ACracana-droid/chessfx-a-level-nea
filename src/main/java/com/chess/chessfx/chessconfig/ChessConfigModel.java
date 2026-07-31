package com.chess.chessfx.chessconfig;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class ChessConfigModel {
    // This is a model that can transfer information between both layout builder classes.

    private int height;
    private int width;

    /// There are 3 main game types to consider:
    /// 1. player vs player
    /// 2. player vs bot
    /// 3. bot vs bot
    /// There is more if we consider doing any server based stuff:
    /// 4. peer vs peer (?)
    ///
    StringProperty gamePlayers = new SimpleStringProperty();

    public ChessConfigModel(String propertiesName) {
        Properties properties = new Properties();
        try {
            InputStream stream = getClass().getResourceAsStream(propertiesName);
            properties.load(stream);
        } catch (IOException e) {
            // Should be updated if we allow saving of configs for user.
            // Otherwise, the jar cannot change.
            System.out.println("This should not happen.");
            e.printStackTrace();
        }
        gamePlayers.set(properties.getProperty("gameType"));
        height = Integer.parseInt(properties.getProperty("height"));
        height = Integer.parseInt(properties.getProperty("width"));
    }

    public ChessConfigModel() {
        // Default Constructor
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
