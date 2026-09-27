package com.chessfx.engine.moves;

import com.chessfx.engine.board.GameStateModel;
import com.chessfx.engine.board.Team;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class MoveTableData {

    private final GameStateModel gameModel;
    private final IntegerProperty fullmoveCounterProperty;
    private final StringProperty previousMoveProperty;
    private Runnable removeListeners;

    public MoveTableData(GameStateModel gameModel, IntegerProperty fullmoveCounterProperty,
                         StringProperty previousMoveProperty) {

        this.gameModel = gameModel;
        this.fullmoveCounterProperty = fullmoveCounterProperty;
        this.previousMoveProperty = previousMoveProperty;
    }

    public IntegerProperty fullmoveCounterProperty() {
        return fullmoveCounterProperty;
    }

    public StringProperty previousMoveProperty() {
        return previousMoveProperty;
    }

    public int getPlyCount() {
        return gameModel.getPlyCount();
    }

    public int getTeamIdx() {
        return gameModel.getCurrentTeamIdx();
    }

    public List<Team> getTeams() {
        return gameModel.getTeams();
    }

    public void removeListeners() {
        if (removeListeners != null)
            removeListeners.run();
    }

    public Runnable getRemoveListenersRunnable() {
        return removeListeners;
    }

    public void setRemoveListenersRunnable(Runnable r) {
        removeListeners = r;
    }

    public int getMoveTableDifference() {
        return gameModel.getFullmoveDifference();
    }

    public boolean isMoveUndo() {
        return gameModel.isMoveUndo();
    }
}
