package com.chessfx.chessconfig;

import com.chessfx.engine.pieces.*;
import com.chessfx.engine.tiles.TileParent;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class ChessConfigModel {
    // This is a model that can transfer information between both layout builder classes.

    private int height;
    private int width;


    private FenManager fen;
    private ArbitraryPieceFactory pieceFactory;
    private TeamConf teamConf;

    private Map<String, PieceData> pieceMap;
    private Map<String, String> fenIdToNameMap;


    private int halfmoveClockRuleNumber;
    private EnPassantRule enPassantRule;
    private int tileCheckerboardOffset;

    private TileParent[][] board;
    String gameType;

    public ChessConfigModel(String propertiesName) {
        setChessConfigFromPropertiesResource(propertiesName);
    }

    // create object without specifying any config.
    public ChessConfigModel() {
        setUpPieceData();
    }

    private void setUpPieceData() {
        CompletableFuture.runAsync(() -> {
            PieceDataManager pieceDataManager = new PieceDataManager();
            pieceMap = pieceDataManager.getPieceDataMap();
            fenIdToNameMap = pieceDataManager.getFenIdToNameMap();
            pieceFactory = new ArbitraryPieceFactory(pieceMap);
        });
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



        height = Integer.parseInt(properties.getProperty("height"));
        width = Integer.parseInt(properties.getProperty("width"));
        teamConf = new TeamConf(properties.getProperty("teamInfo", "wd bu"));
        fen = new FenManager(properties.getProperty("fenPieceTeamIndicator"),
                properties.getProperty("fen"), fenIdToNameMap, pieceFactory, height, width, teamConf);
        halfmoveClockRuleNumber = Integer.parseInt(properties.getProperty("halfmoveClockRuleNumber"));
        // if halfmoveClockRuleNumber == 0, this is equivalent to disabling the halfmove clock
        enPassantRule = EnPassantRule.parseRule(properties.getProperty("enPassantRule"));
        // can change how en passant works

        // 1 or 0
        tileCheckerboardOffset = Integer.parseInt(properties.getProperty("tileCheckerboardOffset"));

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
        return fen.fullmoveNumberProperty();
    }

    public int getHalfmoveNumber() {
        return fen.halfmoveClockProperty();
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

    public List<Team> getTeams() {
        return teamConf.getTeams();
    }

    public int getStartingTeamIdx() {
        return fen.getStartingTeamIdx();
    }

    public Map<String, PieceData> getPieceDataMap() {
        return pieceMap;
    }

    public void setGameType(String s) {
        gameType = s;
    }
}
