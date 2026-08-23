package com.chessfx.ui.game;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.ChessGameModel;
import com.chessfx.ui.menu.ReturnButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.util.Builder;


public class GameLayoutBuilder implements Builder<Region> {

    Runnable changeLayoutToMenu;
    BorderPane rootPane;
    private ChessGameModel boardModel;
    ChessConfigModel chessConfig;

    public GameLayoutBuilder(Runnable changeLayoutToMenu, ChessConfigModel chessConfig) {
        this.changeLayoutToMenu = changeLayoutToMenu;
        this.chessConfig = chessConfig;
        rootPane = new BorderPane();
        warmUpHeavyClasses();
    }


    /**
     * This is because I was running into lag. Because java performs JIT, apparently it may be
     * a good idea to force the classes to compile before loading them with user input. This method does just that.
     */
    private void warmUpHeavyClasses() {
        rootPane.setTop(makeGameTopWrapper(new SimpleIntegerProperty()));
        rootPane.setCenter(new ChessBoardBuilder().build());
        rootPane.layout();
        rootPane.applyCss();
    }

    private HBox makeTurnCounterHBox(IntegerProperty turnCountProperty) {
        HBox hBox = new HBox(makeTimer(), makeTurnCounter(turnCountProperty));
        hBox.getStyleClass().add("game-top-hbox");
        return hBox;
    }


    private Node makeTurnCounter(IntegerProperty integerProperty) {
        Label label = new Label();
        label.textProperty().bind(integerProperty.asString());
        label.getStyleClass().add("turn-counter");
        return label;
    }

    private Node makeTimer() {
        Label label = new Label("00:00");
        // Placeholder for now
        return label;
    }

    @Override
    public Region build() {

//        rootPane.setOnMouseClicked(event -> handleGlobalInGameMouseClick());
        return rootPane;
    }


    /**
     * <b>The entry point for the chess game.</b>
     * This is where a user is directed after pressing a play button.
     * <p>
     * Due to threading, this likely happens during the `manageLayoutAnimation()`
     * in RootWrapperBuilder.
     */
    public void startup(Runnable fadeInLayout) {
        Task<Void> loadBoardModel = new Task<>() {
            @Override
            protected Void call() throws Exception {
                boardModel = new ChessGameModel(chessConfig);
                return null;
            }
        };
        loadBoardModel.setOnSucceeded(event -> {
            rootPane.setCenter(new ChessBoardBuilder(boardModel).build());
            rootPane.setTop(makeGameTopWrapper(boardModel.fullmoveCounterProperty()));
            rootPane.setBottom(makeTurnLabel(boardModel.currentTeamStringProperty()));
            rootPane.setRight(makeMoveTableSideBar(
                    new MoveTable(boardModel.currentMoveProperty(),
                            boardModel.fullmoveCounterProperty(),
                            boardModel.currentTeamStringProperty(),
                            boardModel.getTeams())
            ));

            fadeInLayout.run();
        });
        Thread thread = new Thread(loadBoardModel);
        thread.start();
    }

    private Node makeMoveTableSideBar(MoveTable moveTable) {
        Button button = new Button("<");
        button.setOnAction(event -> moveTable.setManaged(!moveTable.isManaged()));
        return new HBox(button, moveTable);
    }

    private Node makeGameTopWrapper(IntegerProperty fullmoveCounterProperty) {
        StackPane pane = new StackPane(
                makeTurnCounterHBox(fullmoveCounterProperty),
                new ReturnButton(changeLayoutToMenu, "Exit").getAsVBox()
        );

        pane.getStyleClass().add("game-top-wrapper");
        return pane;
    }


    private Label makeTurnLabel(StringProperty currentTeamStringProperty) {

        Label label = new Label();
        label.textProperty().bind(Bindings.concat(currentTeamStringProperty, "'s turn"));
        label.getStyleClass().add("current-turn-label");
        return label;
    }

}
