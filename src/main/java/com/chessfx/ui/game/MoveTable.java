package com.chessfx.ui.game;

import com.chessfx.engine.pieces.Team;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;


public class MoveTable extends TableView<MoveRecord> {

    private final StringProperty currentMoveProperty;
    private final IntegerProperty fullmoveNumProperty;
    private final StringProperty currentTeamProperty;
    int fullmoveNum;
    MoveRecord current;
    int index = 0;
    List<Team> teams;
    ObservableList<MoveRecord> moveRecords =
            FXCollections.observableArrayList();

    public MoveTable(StringProperty currentMoveProperty, IntegerProperty fullmoveNumProperty, StringProperty currentTeamProperty, List<Team> teams) {
        super();
        this.setManaged(false);

        this.currentMoveProperty = currentMoveProperty;
        this.fullmoveNumProperty = fullmoveNumProperty;
        this.currentTeamProperty = currentTeamProperty;
        this.teams = teams;

        fullmoveNum = fullmoveNumProperty.get();
        current = new MoveRecord(fullmoveNum, new StringProperty[teams.size()]);
        currentMoveProperty.addListener(inv -> handleMoveMade());

//        this.getColumns().addAll(makeColumns());
        makeColumns(this.getColumns());
        this.setItems(moveRecords);
        moveRecords.add(current);
        System.out.println(this.getColumns());

        this.setMouseTransparent(true);
    }

    private void handleMoveMade() {
        System.out.println("MoveTable: moveProperty changed");
        if (fullmoveNumProperty.get() == fullmoveNum) {
            current.setMove(index++, currentMoveProperty.get());
        } else {
            current = new MoveRecord(fullmoveNumProperty.get(), new StringProperty[teams.size()]);
            index = 0;
            moveRecords.add(current);
            fullmoveNum = fullmoveNumProperty.get();
        }
    }


    private void makeColumns(ObservableList<TableColumn<MoveRecord, ?>> tableColumns) {
        TableColumn<MoveRecord, Number> index = new TableColumn<>("#");
        index.setCellValueFactory(cellData -> cellData.getValue().fullmoveNumProperty());
//        index.getStyleClass().add("move-index");
        tableColumns.add(index);

        for (int i = 0; i < teams.size(); i++) {
            final int idx = i;
            TableColumn<MoveRecord, String> column = new TableColumn<>(teams.get(idx).getStyleClassString());
            column.setCellValueFactory(cellData ->
                    (cellData.getValue().getTeamMoves(idx)));
            tableColumns.add(column);
        }
    }


}

class MoveRecord {

    StringProperty[] teamMoves;
    final IntegerProperty fullmoveNumProperty;

    public MoveRecord(int fullmoveNum, StringProperty[] teamMoves) {
        this.teamMoves = teamMoves;
        System.out.println("initialising move list");
        for (int i = 0; i < teamMoves.length; i++) {
            teamMoves[i] = new SimpleStringProperty();
        }
        this.fullmoveNumProperty = new SimpleIntegerProperty(fullmoveNum);
    }

    public IntegerProperty fullmoveNumProperty() {
        return fullmoveNumProperty;
    }

    public StringProperty getTeamMoves(int idx) {
        return teamMoves[idx];
    }

    public void setMove(int idx, String moveStr) {
        teamMoves[idx].set(moveStr);
    }
}