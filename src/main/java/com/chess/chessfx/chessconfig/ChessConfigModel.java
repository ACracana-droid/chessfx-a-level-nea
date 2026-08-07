package com.chess.chessfx.chessconfig;

import com.chess.chessfx.engine.tiles.TileParent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

public class ChessConfigModel {
    // This is a model that can transfer information between both layout builder classes.

    private int height;
    private int width;
    private FenManager fen;
    private int halfmoveClockRuleNumber;
    private EnPassantRule enPassantRule;
    private int tileCheckerboardOffset;
    private TileParent[][] board;

    private String allPiecesInGame;
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
//        System.out.println(properties.getProperty("fen"));
        fen = new FenManager(properties.getProperty("fenPieceTeamIndicator"),
                properties.getProperty("fen"));
        height = Integer.parseInt(properties.getProperty("height"));
        width = Integer.parseInt(properties.getProperty("width"));
        halfmoveClockRuleNumber = Integer.parseInt(properties.getProperty("halfmoveClockRuleNumber"));
        // if halfmoveClockRuleNumber == 0, this is equivalent to disabling the halfmove clock
        enPassantRule = EnPassantRule.parseRule(properties.getProperty("enPassantRule"));
        // can change how en passant works

        // 1 or 0
        tileCheckerboardOffset = Integer.parseInt(properties.getProperty("tileCheckerboardOffset"));

        // these must be in this order.
        board = fen.getGameBoard(height, width);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public TileParent[][] getGameBoard() {
        return board;
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

    public int getTileCheckerboardOffset() {
        return tileCheckerboardOffset;
    }

    public Set<String> getInstantiatedPiecesIdSet() {
        return fen.getInstantiatedPieceIdSet();
    }
}
