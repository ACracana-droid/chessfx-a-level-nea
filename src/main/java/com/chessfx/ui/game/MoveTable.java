package com.chessfx.ui.game;

import com.chessfx.engine.moves.MoveTableData;
import com.chessfx.log.Log;
import javafx.beans.InvalidationListener;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;


public class MoveTable extends StackPane {

    MoveTableData ref;

    GridPane grid = new GridPane();
    //    int rowIdx = 1;
    final int START_FULLMOVE_NUMBER;
    private final Button hideTableButton;
    private final Button showTableButton;

    public MoveTable(MoveTableData ref, Button hideTableButton, Button showTableButton) {
        super();
        this.ref = ref;
        this.hideTableButton = hideTableButton;

        START_FULLMOVE_NUMBER = ref.fullmoveCounterProperty().get();
        this.ref.setRemoveListenersRunnable(initialiseListeners());
        this.getStyleClass().add("move-table");

        ScrollPane scrollPane = makeTableScroller();
        this.showTableButton = makeShowTableButton(scrollPane, showTableButton);
        this.getChildren().add(scrollPane);
        makeSideBarToggleButton(hideTableButton, scrollPane);

        grid.add(rowLabel(this.ref.fullmoveCounterProperty().get()), 0, rowIdx());
    }

    private Button makeShowTableButton(ScrollPane scrollPane, Button showTableButton) {
        showTableButton.setOnAction(event -> {
            boolean isVisible = scrollPane.isVisible();

            this.getChildren().remove(this.showTableButton);
            boolean b = !isVisible;
            scrollPane.setVisible(b);
            scrollPane.setManaged(b);
        });
        return showTableButton;
    }

    private ScrollPane makeTableScroller() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(grid = makeTableGridPane());
        // scrollPane goes to :focused when clicked...
        // To remedy this I've overridden in css.
        return scrollPane;
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
        Log.debug("MoveTable", "FULL MOVE NUM CHANGED: " + difference);
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
            return row > rowIdx;
        });
    }


    private void handleMovePropertyChanged(IntegerProperty fullmoveNumProperty, StringProperty currentMoveProperty) {
        Log.debug("MoveTable", "Move property was changed. Actioning...");
        boolean wasUndo = ref.isMoveUndo();
        if (wasUndo) {
            removeGridEntry(ref.getTeamIdx() + 1, rowIdx());
            return;
        }

        addMove(ref.getTeamIdx(), currentMoveProperty);
    }

    private void removeGridEntry(int col, int row) {
        grid.getChildren().removeIf(node -> {
            int colIndex = GridPane.getColumnIndex(node);
            int rowIndex = GridPane.getRowIndex(node);

            return colIndex == col && rowIndex == row;
        });
    }

    private void addRowNumber(int fullmoveNum, int rowIdx) {
        grid.add(rowLabel(fullmoveNum), 0, rowIdx);
    }

    private Node rowLabel(int num) {
        Label label = new Label(num + "");
        label.getStyleClass().add("row-label");
        label.getStyleClass().add("table-move");
        if (rowIdx() % 2 == 0) {
            label.getStyleClass().add("move-1");
        } else {
            label.getStyleClass().add("move-2");
        }
        return label;
    }

    private GridPane makeTableGridPane() {
        GridPane grid = new GridPane();
        grid.add(hideTableButton, 0, 0);
        grid.setFocusTraversable(false);
        for (int i = 0; i < ref.getTeams().size(); i++) {
            grid.add(headerLabel(ref.getTeams().get(i).getName()), i + 1, 0);
        }
        grid.getStyleClass().add("grid");
        return grid;
    }


    private Node headerLabel(String s) {
        Pane labelPane = new StackPane(new Label(s));
        labelPane.getStyleClass().add("table-label");
        return labelPane;
    }

    private Button makeSideBarToggleButton(Button tableVisibilityButton, Node tableScroller) {
        tableVisibilityButton.getStyleClass().add("sidebar-button");
        tableVisibilityButton.setOnAction(event -> {
            boolean isVisible = tableScroller.isVisible();

            this.getChildren().add(showTableButton);
            boolean b = !isVisible;
            tableScroller.setVisible(b);
            tableScroller.setManaged(b);
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
        Pane labelPane = new Pane(new Label(s));
        labelPane.getStyleClass().add("table-move");
        if (rowIdx() % 2 == 0) {
            labelPane.getStyleClass().add("move-1");
        } else {
            labelPane.getStyleClass().add("move-2");
        }
        return labelPane;
    }

}
