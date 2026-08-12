package com.chess.chessfx.chessconfig;

import com.chess.chessfx.engine.pieces.ArbitraryPieceFactory;
import com.chess.chessfx.engine.pieces.PieceData;
import com.chess.chessfx.engine.pieces.PieceDataManager;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.engine.tiles.TileParent;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ChessConfigModel {
    // This is a model that can transfer information between both layout builder classes.

    private int height;
    private int width;

    private FenManager fen;
    private ArbitraryPieceFactory pieceFactory;

    private Map<String, PieceData> pieceMap;
    private Map<String, String> fenIdToNameMap;


    private int halfmoveClockRuleNumber;
    private EnPassantRule enPassantRule;
    private int tileCheckerboardOffset;
    private TileParent[][] board;


    // create object without specifying any config.
    public ChessConfigModel() {
        setUpPieceData();
    }

    private void setUpPieceData() {
        PieceDataManager pieceDataManager = new PieceDataManager();
        pieceMap = pieceDataManager.getPieceDataMap();
        fenIdToNameMap = pieceDataManager.getFenIdToNameMap();
        pieceFactory = new ArbitraryPieceFactory(pieceMap);
    }


    public void setChessConfigFromPropertiesResource(String propertiesName) {
        Properties properties = new Properties();
        try {
            InputStream stream = getClass().getResourceAsStream(propertiesName);
            properties.load(stream);
        } catch (IOException e) {
            System.out.println("This should not happen.\npropertiesName: " + propertiesName);
            e.printStackTrace();
        }

//        System.out.println(properties.getProperty("fen"));


        height = Integer.parseInt(properties.getProperty("height"));
        width = Integer.parseInt(properties.getProperty("width"));
        fen = new FenManager(properties.getProperty("fenPieceTeamIndicator"),
                properties.getProperty("fen"), fenIdToNameMap, pieceFactory, height, width);
        halfmoveClockRuleNumber = Integer.parseInt(properties.getProperty("halfmoveClockRuleNumber"));
        // if halfmoveClockRuleNumber == 0, this is equivalent to disabling the halfmove clock
        enPassantRule = EnPassantRule.parseRule(properties.getProperty("enPassantRule"));
        // can change how en passant works

        // 1 or 0
        tileCheckerboardOffset = Integer.parseInt(properties.getProperty("tileCheckerboardOffset"));

        // these must be in this order.
        board = fen.getGameBoard();
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

    public Set<String> getInstantiatedPiecesNameSet() {
        return fen.getInstantiatedPieceNameSet();
    }

    public Team getStartingTeam() {
        return fen.getStartingTeam();
    }
}
