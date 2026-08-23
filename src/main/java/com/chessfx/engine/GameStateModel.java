package com.chessfx.engine;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.pieces.PieceData;
import com.chessfx.engine.pieces.Team;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameStateModel {

    private final int HEIGHT;
    private final int WIDTH;
    private final ChessConfigModel chessConfig;

    private int fullmoveCounter;
    int plyCount;
    int halfmoveClock; // used for 50 move draw rule (since last capture or pawn advance)
    int halfmoveClockRuleNumber; // used for changing '50' halfmove draw rule


    private final List<Team> teams;
    private int teamIdx;
    boolean cycleTeamEndFlag = false;

    private BoardState boardState;

    public GameStateModel(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;

        HEIGHT = chessConfig.getHeight();
        WIDTH = chessConfig.getWidth();


        // for each game instance.
        plyCount = 0;

        fullmoveCounter = (chessConfig.getFullmoveNumber());
        halfmoveClock = (chessConfig.getHalfmoveNumber());
        halfmoveClockRuleNumber = chessConfig.getHalfmoveClockRuleNumber();
        teams = chessConfig.getTeams();
        teamIdx = (chessConfig.getStartingTeamIdx());
    }

    Team getCurrentTeam() {
        return teams.get(teamIdx);
    }

    public int getHeight() {
        return HEIGHT;
    }

    public int getWidth() {
        return WIDTH;
    }

    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Cycles the game after each player's turn.
     * When all players have had a turn, the game returns to the first player's turn until the game ends.
     *
     * @return true if cycle restarts, false otherwise
     */
    public boolean cycleTeam() {
        teamIdx++;
        cycleTeamEndFlag = false;

        if (teamIdx >= teams.size()) {
            teamIdx = (0);
            fullmoveCounter++;
            System.out.println("Incrementing fullmoveCounter. New value: " + fullmoveCounter);
            cycleTeamEndFlag = true;
        }

        halfmoveClock--; // must be reset after a pawn move or capture.

        return cycleTeamEndFlag;
    }

    public boolean isTeamEndOfCycle() {
        return cycleTeamEndFlag;
    }

    public void resetHalfMoveCounter() {
        halfmoveClock = halfmoveClockRuleNumber;
    }

    public int getFullmoveCounter() {
        return fullmoveCounter;
    }

    public int getCurrentTeamIdx() {
        return teamIdx;
    }

    public int getPlyCount() {
        return plyCount;
    }

    public void setBoardState(BoardState state) {
        boardState = state;
    }

    public Team getNextTeam() {
        return teams.get((teamIdx + 1) % teams.size());
    }

    public Map<String, PieceData> getAvailableMoveSets() {
        return chessConfig.getPieceDataMap();
    }

    public Set<String> getInstantiatedPieceNames() {
        return chessConfig.getInstantiatedPiecesNameSet();
    }
}
