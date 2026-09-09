package com.chessfx.ui.game;

import com.chessfx.engine.MoveTableData;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;


public class MoveTable extends StackPane {

    MoveTableData ref;

    GridPane grid = new GridPane();
    int rowIdx = 1;


    public MoveTable(MoveTableData ref) {
        super();
        this.ref = ref;
        this.ref.setRemoveListenersRunnable(initialiseListeners());

        ScrollPane tableScroller = new ScrollPane();
        tableScroller.setContent(makeTableGridPane());

        grid.add(rowLabel(this.ref.fullmoveCounterProperty().get()), 0, rowIdx);

        this.getChildren().add(makeHBox(tableScroller));
    }

    /**
     * Creates the listeners on the properties from the ReactiveChessContainer class
     * (brokered by the MoveTableData class) which allow this class (MoveTable) to instantly
     * adapt to any game updates.
     *
     * @return removeListenersRunnable runnable to remove the listeners in this class.
     */
    private Runnable initialiseListeners() {
        ChangeListener<Number> fullmoveNumListener = (observable, oldValue, newValue) -> handleFullmoveNumChange(newValue);
        ref.fullmoveCounterProperty().addListener(fullmoveNumListener);

        InvalidationListener currentMoveInvalidationListener = inv ->
                handleMoveMade(ref.fullmoveCounterProperty(), ref.previousMoveProperty());
        ref.previousMoveProperty().addListener(currentMoveInvalidationListener);

        return () -> removeListeners(ref.fullmoveCounterProperty(), fullmoveNumListener,
                ref.previousMoveProperty(), currentMoveInvalidationListener);
    }

    private void removeListeners(IntegerProperty fullmoveNumProperty, ChangeListener<Number> fullMoveNumListener, StringProperty currentMoveProperty, InvalidationListener currentMoveInvalidationListener) {
        fullmoveNumProperty.removeListener(fullMoveNumListener);
        currentMoveProperty.removeListener(currentMoveInvalidationListener);
    }

    private void handleFullmoveNumChange(Number fullmoveNum) {
        System.out.println("MOVE TABLE: FULL MOVE NUM CHANGED");
        addRowNumber(fullmoveNum.intValue(), rowIdx + 1);
    }


    private void handleMoveMade(IntegerProperty fullmoveNumProperty, StringProperty currentMoveProperty) {
        System.out.println("MoveTable: moveProperty changed");
        if (ref.isTeamEndOfCycle()) {
            rowIdx++;
        }
        addMove(ref.getTeamIdx(), currentMoveProperty);
    }

    private void addRowNumber(int fullmoveNum, int rowIdx) {
        grid.add(rowLabel(fullmoveNum), 0, rowIdx);
    }

    private Node rowLabel(int num) {
        Label label = new Label(num + "");
        label.getStyleClass().add("row-label");
        return label;
    }

    private Node makeTableGridPane() {
        grid = new GridPane();
        grid.getStyleClass().add("move-table");
        grid.add(headerLabel("#"), 0, 0);
        for (int i = 0; i < ref.getTeams().size(); i++) {
            grid.add(headerLabel(ref.getTeams().get(i).getName()), i + 1, 0);
        }
        return grid;
    }

    private Node headerLabel(String s) {
        Label label = new Label(s);
        label.getStyleClass().add("table-label");
        return label;
    }


    private Node makeHBox(ScrollPane tableScroller) {
        HBox hBox = new HBox(makeSideBarToggleButton(tableScroller), tableScroller);
        hBox.getStyleClass().add("movetable-hbox");
        return hBox;
    }

    private Button makeSideBarToggleButton(Node tableScroller) {
        Button button = new Button("<");
        button.getStyleClass().add("sidebar-button");
        button.setOnAction(event -> tableScroller.setManaged(!tableScroller.isManaged()));
        return button;
    }


    private void addMove(int teamIdx, StringProperty currentMoveProperty) {
        grid.add(moveLabel(currentMoveProperty.get()), teamIdx + 1, rowIdx);
    }

    private Node moveLabel(String s) {
        Label label = new Label(s);
        label.getStyleClass().add("table-move");
        return label;
    }

}
