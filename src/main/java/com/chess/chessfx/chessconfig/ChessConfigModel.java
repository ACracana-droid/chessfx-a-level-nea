package com.chess.chessfx.chessconfig;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ChessConfigModel {
    // This is a model that can transfer information between both layout builder classes.

    private int height;
    private int width;
    private ChangedFenManager fen;

    /// There are 3 main game types to consider:
    /// 1. player vs player
    /// 2. player vs bot
    /// 3. bot vs bot
    /// There is more if we consider doing any server based stuff:
    /// 4. peer vs peer (?)
    ///
    private final StringProperty gamePlayers = new SimpleStringProperty();

    public ChessConfigModel(String propertiesName) {
        Properties properties = new Properties();
        try {
            InputStream stream = getClass().getResourceAsStream(propertiesName);
            properties.load(stream);
        } catch (IOException e) {
            System.out.println("This should not happen.\npropertiesName: " + propertiesName);
            e.printStackTrace();
        }

        gamePlayers.set(properties.getProperty("gameType"));
        fen = new ChangedFenManager(properties.getProperty("changedFEN"));
        height = Integer.parseInt(properties.getProperty("height"));
        width = Integer.parseInt(properties.getProperty("width"));
    }

    public ChessConfigModel() {
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public final String getGamePlayers() {
        return gamePlayers.get();
    }

    public final StringProperty gamePlayersProperty() {
        return gamePlayers;
    }

    public ChangedFenManager getFen() {
        return fen;
    }
}
