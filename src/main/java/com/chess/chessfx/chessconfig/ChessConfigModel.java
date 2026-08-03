package com.chess.chessfx.chessconfig;

import com.chess.chessfx.engine.Tile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ChessConfigModel {
    // This is a model that can transfer information between both layout builder classes.

    private int height;
    private int width;
    private FenManager fen;
    private int halfmoveClockRuleNumber;
    private EnPassantRule enPassantRule;

    /// There are 3 main game types to consider:
    /// 1. player vs player
    /// 2. player vs bot
    /// 3. bot vs bot
    /// There is more if we consider doing any server based stuff:
    /// 4. peer vs peer (?)
    ///
    private String gamePlayers;

    // create object without specifying any config.
    public ChessConfigModel() {
    }

    // used to be a constructor, works better as method.
    public void setChessConfigFromPropertiesResource(String propertiesName) {
        Properties properties = new Properties();
        try {
            InputStream stream = getClass().getResourceAsStream(propertiesName);
            properties.load(stream);
        } catch (IOException e) {
            System.out.println("This should not happen.\npropertiesName: " + propertiesName);
            e.printStackTrace();
        }

        gamePlayers = (properties.getProperty("gameType"));
        fen = new FenManager(properties.getProperty("fenPieceTeamIndicator"), properties.getProperty("fen"));
        height = Integer.parseInt(properties.getProperty("height"));
        width = Integer.parseInt(properties.getProperty("width"));
        halfmoveClockRuleNumber = Integer.parseInt(properties.getProperty("halfmoveClockRuleNumber"));
        // if halfmoveClockRuleNumber == 0, this is equivalent to disabling the halfmove clock
        enPassantRule = EnPassantRule.parseRule(properties.getProperty("enPassantRule"));
        // can change how en passant works


    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Tile[][] getGameBoard() {
        return fen.getGameBoard(height, width);
    }

    public EnPassantRule getEnPassantRule() {
        return enPassantRule;
    }

    public int getFullmoveNumber() {
        return fen.getFullmoveNumber();
    }

    public int getHalfmoveClock() {
        return fen.getHalfmoveClock();
    }

    public int getHalfmoveClockRuleNumber() {
        return halfmoveClockRuleNumber;
    }
}
