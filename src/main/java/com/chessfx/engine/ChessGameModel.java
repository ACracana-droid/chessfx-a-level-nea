package com.chessfx.engine;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.pieces.Team;
import com.chessfx.engine.tiles.TileParent;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class ChessGameModel {

    private final IntegerProperty fullmoveCounter;
    private final IntegerProperty halfmoveClock; // used for 50 move draw rule (since last capture or pawn advance)
    int halfmoveClockRuleNumber; // used for changing '50' halfmove draw rule

    public StringProperty currentMoveProperty = new SimpleStringProperty();
    private Team currentTeam;
    private final List<Team> teams;
    private int teamIdx;
    StringProperty teamProperty = new SimpleStringProperty();

    private final ChessConfigModel chessConfig;

    TileParent[][] board;

    public ChessGameModel(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;

        board = chessConfig.getGameBoard();
        fullmoveCounter = new SimpleIntegerProperty(chessConfig.getFullmoveNumber());
        halfmoveClock = new SimpleIntegerProperty(chessConfig.getHalfmoveNumber());
        halfmoveClockRuleNumber = chessConfig.getHalfmoveClockRuleNumber();
        teams = chessConfig.getTeams();
        teamIdx = chessConfig.getStartingTeamIdx();
        currentTeam = teams.get(teamIdx);
        teamProperty.set(currentTeam.toString());
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
        return teams.get(teamIdx);
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

    public void doMove(Move move) {
        Piece piece = board[move.y1][move.x1].removePiece();
        piece.setCoOrds(move.y2, move.x2);
        piece.setHasMoved(true);
        if (move.isCapture) {
            // for things like en passant
            if (move.hasCapturePiece()) {
                currentTeam.addCapture(move.capture);
                // handle capture
            }
        }
        board[move.y2][move.x2].setPiece(piece);
        updateGame(move);
    }

    private void updateGame(Move move) {
        System.out.println("UPDATING GAME: ");
        halfmoveClock.set(halfmoveClock.get() + 1);
        currentMoveProperty.set(move.algebraicNotation());
        switchTeams();
    }


    private void switchTeams() {
        teamIdx++;
        if (teamIdx < teams.size()) {
            currentTeam = teams.get(teamIdx);

        } else {
            teamIdx = 0;
            fullmoveCounter.set(fullmoveCounter.get() + 1);
            System.out.println("Incrementing fullmoveCounter. New value: " + fullmoveCounter.get());
            currentTeam = teams.get(teamIdx);
        }
        teamProperty.set(currentTeam.toString());
    }

    public StringProperty currentTeamStringProperty() {
        return teamProperty;
    }

    public StringProperty currentMoveProperty() {
        return currentMoveProperty;
    }

    public List<Team> getTeams() {
        return teams;
    }
}
