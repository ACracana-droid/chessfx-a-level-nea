package com.chessfx.engine.board;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameStateModel implements Cloneable {

    private final ChessConfigModel chessConfig;

    private final int halfmoveClockRuleNumber; // used for changing '50' halfmove draw rule

    private int fullmoveCounter;
    private int plyCount;
    private int halfmoveClock; // used for 50 move draw rule (since last capture or pawn advance)

    private final List<Team> teams;
    private int teamIdx;
    FullmoveDifference fullmoveDifference = FullmoveDifference.SAME;
    private boolean isUndo = true;

    private BoardState boardState = BoardState.DEFAULT; // This is bad for cases where the starting board state is check or something...
    private boolean isPromotion;
    private String undoMoveFailedReason = "";

    public GameStateModel(GameStateModel gameModel, List<Team> teamList) {
        chessConfig = gameModel.getChessConfig();
        plyCount = gameModel.getPlyCount();
        fullmoveCounter = gameModel.getFullmoveCounter();
        halfmoveClock = gameModel.halfmoveClock;
        halfmoveClockRuleNumber = gameModel.halfmoveClockRuleNumber;
        teamIdx = gameModel.getCurrentTeamIdx();
        isUndo = gameModel.isUndo;
        isPromotion = gameModel.isPromotion();
        fullmoveDifference = gameModel.fullmoveDifference;
        boardState = gameModel.getBoardState();
        undoMoveFailedReason = gameModel.getUndoMoveFailedReason();
        this.teams = teamList;
    }

    public String getUndoMoveFailedReason() {
        return undoMoveFailedReason;
    }

    public Team getEnemyTeam() {
        return getNextTeam();
    }

    public List<Team> copyTeams() {
        List<Team> copy = new ArrayList<>();
        for (int i = 0; i < teams.size(); i++) {
            copy.add(new Team(teams.get(i)));
        }
        return copy;
    }

    public boolean isGameOver() {
        return boardState.equals(BoardState.CHECKMATE) || boardState.equals(BoardState.STALEMATE);
    }

    public void setIsPromotion(boolean isPromotion) {
        this.isPromotion = isPromotion;
    }

    public boolean isPromotion() {
        return isPromotion;
    }

    public void setUndoMoveFailedReason(String msg) {
        undoMoveFailedReason = msg;
    }

    enum FullmoveDifference {
        LESS(-1),
        SAME(0),
        MORE(+1);
        final int diff;

        FullmoveDifference(int diff) {
            this.diff = diff;
        }

        public int getValue() {
            return diff;
        }
    }


    public GameStateModel(ChessConfigModel chessConfig) {
        this.chessConfig = chessConfig;

        plyCount = 0;

        fullmoveCounter = (chessConfig.getFullmoveNumber());
        halfmoveClock = (chessConfig.getHalfmoveNumber());
        halfmoveClockRuleNumber = chessConfig.getHalfmoveClockRuleNumber();
        teams = chessConfig.getTeams();
        teamIdx = (chessConfig.getStartingTeamIdx());
    }

    public Team getCurrentTeam() {
        return teams.get(teamIdx);
    }


    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Cycles the game after each player's turn.
     * When all players have had a turn, the game returns to the first player's turn until the game ends.
     *
     * @param isForwards whether move is undo or a 'forward' move.
     * @return FullmoveDifference, which is always either SAME or MORE.
     */
    public FullmoveDifference cycleTeam(boolean isForwards) {
        this.isUndo = isForwards;
        if (isForwards)
            return cycleTeamForwards();
        else
            return cycleTeamBackwards();
    }

    private FullmoveDifference cycleTeamForwards() {
        teamIdx++;
        fullmoveDifference = FullmoveDifference.SAME;


        if (teamIdx >= teams.size()) {
            teamIdx = (0);
            fullmoveCounter++;
            Log.info("Incrementing fullmoveCounter. New value: " + fullmoveCounter);
            fullmoveDifference = FullmoveDifference.MORE;
        }
        Log.debug("GameModel", "Team index is " + teamIdx);

        halfmoveClock--; // must be reset after a pawn move or capture.

        return fullmoveDifference;
    }

    private FullmoveDifference cycleTeamBackwards() {
        teamIdx--;
        Log.debug("Undoing move, cycling team backwards.");
        fullmoveDifference = FullmoveDifference.SAME;
        if (teamIdx < 0) {
            teamIdx = teams.size() - 1;
            fullmoveCounter--;
            Log.info("Decrementing fullmoveCounter. New value: " + fullmoveCounter);
            fullmoveDifference = FullmoveDifference.LESS;
        }

        Log.debug("GameModel", "Team index is " + teamIdx);
        halfmoveClock++; // must be reset after a pawn move or capture.

        return fullmoveDifference;
    }

    public boolean isMoveUndo() {
        return isUndo;
    }

    public void setIsMoveUndo(boolean b) {
        isUndo = b;
    }

    @Override
    public GameStateModel clone() {
        try {
            GameStateModel clone = (GameStateModel) super.clone();

            // Must copy mutable state here, so the clone can't change the internals of the original
            clone.fullmoveCounter = fullmoveCounter;
            clone.plyCount = plyCount;
            clone.halfmoveClock = halfmoveClock;
            clone.teamIdx = teamIdx;
            clone.fullmoveDifference = fullmoveDifference;
            clone.isUndo = isUndo;
            clone.boardState = boardState;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public BoardState getBoardState() {
        return boardState;
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

    public int getNextTeamIdx() {
        return (teamIdx + 1) % teams.size();
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


    public Set<String> getInstantiatedPieceNames() {
        return chessConfig.getInstantiatedPiecesNameSet();
    }


    /**
     * @return -1 if number of fullmoves decreased, 0 if number of fullmoves is the same and
     * 1 if the number of fullmoves increased.
     */
    public int getFullmoveDifference() {
        return fullmoveDifference.getValue();
    }

    public ChessConfigModel getChessConfig() {
        return chessConfig;
    }
}
