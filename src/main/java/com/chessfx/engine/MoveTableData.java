package com.chessfx.engine;

import com.chessfx.engine.pieces.Team;
import javafx.beans.InvalidationListener;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

import java.util.List;

public class MoveTableData {

    private final GameStateModel gameModel;
    private final IntegerProperty fullmoveCounterProperty;
    private final StringProperty previousMoveProperty;
    private Runnable removeListeners;

    public MoveTableData(GameStateModel gameModel, IntegerProperty fullmoveCounterProperty
            , StringProperty previousMoveProperty) {

        this.gameModel = gameModel;
        this.fullmoveCounterProperty = fullmoveCounterProperty;
        this.previousMoveProperty = previousMoveProperty;
    }


    public IntegerProperty fullmoveCounterProperty() {
        return fullmoveCounterProperty;
    }

    public boolean isTeamEndOfCycle() {
        return gameModel.isTeamEndOfCycle();
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
}
