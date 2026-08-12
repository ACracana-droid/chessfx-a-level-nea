package com.chess.chessfx.engine;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.engine.tiles.TileParent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class ChessGameModel {

    IntegerProperty fullmoveCounter;
    IntegerProperty halfmoveClock; // used for 50 move draw rule (since last capture or pawn advance)
    int halfmoveClockRuleNumber; // used for changing '50' halfmove draw rule

    Team currentTeam;

    private final ChessConfigModel chessConfig;

    TileParent[][] board;

    public ChessGameModel(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;

        board = chessConfig.getGameBoard();

        fullmoveCounter = new SimpleIntegerProperty(chessConfig.getFullmoveNumber());
        halfmoveClock = new SimpleIntegerProperty(chessConfig.getHalfmoveClock());
        halfmoveClockRuleNumber = chessConfig.getHalfmoveClockRuleNumber();
        currentTeam = chessConfig.getStartingTeam();
    }

    public IntegerProperty fullmoveCounterProperty() {
        return fullmoveCounter;
    }

    public TileParent[][] getBoardAsTileArray2D() {
        return board;
    }

    public ChessConfigModel getChessConfig() {
        return chessConfig;
    }

    public Team getCurrentTeam() {
        return currentTeam;
    }

    public boolean inBoardBounds(int y, int x) {
        return 0 <= y && y < chessConfig.getHeight()
                && 0 <= x && x < chessConfig.getHeight();
    }

    public boolean tileHasPiece(int rank, int file) {
        return board[rank][file].hasPiece();
    }

    // shouldn't have to pass the team, it must be currentTeam
    public boolean isPossibleMove(Team team, int rank, int file) {
        return !board[rank][file].hasPieceMatchingTeam(team);
    }

    public boolean tileHasPieceMatchingTeam(int y, int x) {
        return board[y][x].hasPieceMatchingTeam(getCurrentTeam());
    }
}
