package com.chessfx.ui.game;

import com.chessfx.engine.moves.MoveTableData;
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
    //    int rowIdx = 1;
    final int START_FULLMOVE_NUMBER;
    private final Button tableVisibilityButton;


    public MoveTable(MoveTableData ref, Button tableVisibilityButton) {
        super();
        this.ref = ref;
        START_FULLMOVE_NUMBER = ref.fullmoveCounterProperty().get();
        this.tableVisibilityButton = tableVisibilityButton;
        this.ref.setRemoveListenersRunnable(initialiseListeners());

        ScrollPane tableScroller = new ScrollPane();
        tableScroller.setContent(makeTableGridPane());

        grid.add(rowLabel(this.ref.fullmoveCounterProperty().get()), 0, rowIdx());

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
        ChangeListener<Number> fullmoveNumListener = (observable, oldValue, newValue)
                -> handleFullmoveNumChange(newValue.intValue(), oldValue.intValue());
        ref.fullmoveCounterProperty().addListener(fullmoveNumListener);

        InvalidationListener currentMoveInvalidationListener = inv ->
                handleMovePropertyChanged(ref.fullmoveCounterProperty(), ref.previousMoveProperty());
        ref.previousMoveProperty().addListener(currentMoveInvalidationListener);

        return () -> removeListeners(ref.fullmoveCounterProperty(), fullmoveNumListener,
                ref.previousMoveProperty(), currentMoveInvalidationListener);
    }

    private void removeListeners(IntegerProperty fullmoveNumProperty, ChangeListener<Number> fullMoveNumListener, StringProperty currentMoveProperty, InvalidationListener currentMoveInvalidationListener) {
        fullmoveNumProperty.removeListener(fullMoveNumListener);
        currentMoveProperty.removeListener(currentMoveInvalidationListener);
    }

    private void handleFullmoveNumChange(int fullmoveNum, int oldFullmoveNum) {

        int difference = fullmoveNum - oldFullmoveNum;
        System.out.println("MOVE TABLE: FULL MOVE NUM CHANGED: " + difference);
        if (difference == 0) {
            // do nothing
        } else if (difference == 1) {
            addRowNumber(fullmoveNum, rowIdx());
        } else {
            removeRowNumber(rowIdx());
        }
    }

    private void removeRowNumber(int rowIdx) {
        grid.getChildren().removeIf(node -> {
            int row = GridPane.getRowIndex(node);
            if (row > rowIdx) {
                System.out.println("REMOVING col: " + GridPane.getColumnIndex(node).toString() + " row: " + GridPane.getRowIndex(node));
            }
            return row > rowIdx;
        });
    }


    private void handleMovePropertyChanged(IntegerProperty fullmoveNumProperty, StringProperty currentMoveProperty) {
        System.out.println("--".repeat(10) + "MoveTable: moveProperty changed");
        boolean wasUndo = ref.isMoveUndo();
        if (wasUndo) {
            System.out.println("    Removing row...");
            removeGridEntry(ref.getTeamIdx() + 1, rowIdx());
            System.out.println("    Move removing over.");
            return;
        }

        addMove(ref.getTeamIdx(), currentMoveProperty);
    }

    private void removeGridEntry(int col, int row) {
        System.out.println("Trying to remove at col " + col + ", row " + row);
        grid.getChildren().removeIf(node -> {
            int colIndex = GridPane.getColumnIndex(node);
            int rowIndex = GridPane.getRowIndex(node);
            System.out.println("col: " + colIndex + " row: " + rowIndex);

            if (colIndex == col && rowIndex == row)
                System.out.println("REMOVED SUCCESSFULLY!");
            return colIndex == col && rowIndex == row;
        });
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
        tableVisibilityButton.getStyleClass().add("sidebar-button");
        tableVisibilityButton.setOnMouseClicked(event -> {
            tableScroller.setVisible(!tableScroller.isVisible());
            tableScroller.setManaged(!tableScroller.isManaged());
        });
        return tableVisibilityButton;
    }


    private void addMove(int teamIdx, StringProperty currentMoveProperty) {
        grid.add(moveLabel(currentMoveProperty.get()), teamIdx + 1, rowIdx());
    }

    private int rowIdx() {
        return ref.fullmoveCounterProperty().get() - START_FULLMOVE_NUMBER + 1;
    }

    private Node moveLabel(String s) {
        Label label = new Label(s);
        label.getStyleClass().add("table-move");
        return label;
    }

}
