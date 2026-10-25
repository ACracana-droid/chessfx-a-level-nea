package com.chessfx.ui.game;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.board.GameStateModel;
import com.chessfx.engine.board.ReactiveChessContainer;
import com.chessfx.log.Log;
import com.chessfx.ui.game.board.BoardUIModel;
import com.chessfx.ui.game.board.ChessBoardBuilder;
import com.chessfx.chessconfig.AppSettings;
import com.chessfx.ui.menu.LayoutBuilder;
import com.chessfx.ui.util.SettingsLayoutUtil;
import com.chessfx.ui.util.LayoutManager;
import com.chessfx.ui.util.ThemeDropDown;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.*;


public class GameLayoutBuilder extends LayoutBuilder {

    private final BorderPane rootPane;
    private ReactiveChessContainer boardContainer;

    private final ChessConfigModel chessConfig;
    private final AppSettings appSettings;
    private BoardUIModel boardUIModel;

    public GameLayoutBuilder(LayoutManager layoutManager, ChessConfigModel chessConfig, AppSettings appSettings) {
        super(chessConfig, layoutManager, LayoutManager.Layout.GAME);
        this.chessConfig = chessConfig;
        this.appSettings = appSettings;
        rootPane = new BorderPane();
        warmUpHeavyClasses();
    }

    private Runnable endAndReturnAction() {
        return () -> {
            terminate();
            layoutManager.startup(LayoutManager.Layout.MENU);
        };
    }


    /**
     * This is because I was running into lag. Because java performs JIT, apparently it may be
     * a good idea to force the classes to compile before loading them with user input. This method does just that.
     */
    private void warmUpHeavyClasses() {
        rootPane.setTop(makeGameTopWrapper(new SimpleIntegerProperty(), new SimpleStringProperty()));
        rootPane.setCenter(new ChessBoardBuilder().build());
        rootPane.layout();
        rootPane.applyCss();
    }

    private HBox makeTurnCounterHBox(IntegerProperty turnCountProperty, StringProperty boardStateProperty) {
        HBox hBox = new HBox(makeTimer(), makeTurnCounter(turnCountProperty), makeBoardStateLabel(boardStateProperty));
        hBox.getStyleClass().add("game-top-hbox");
        return hBox;
    }

    private Label makeBoardStateLabel(StringProperty boardStateProperty) {
        Label label = new Label();
        label.textProperty().bind(boardStateProperty);
        setUpBoardStatePseudoClasses(boardStateProperty, label);
        label.getStyleClass().add("board-state-label");
        return label;
    }

    private void setUpBoardStatePseudoClasses(StringProperty boardStateProperty, Label label) {
        PseudoClass check = PseudoClass.getPseudoClass("check");
        PseudoClass stalemate = PseudoClass.getPseudoClass("stalemate");
        PseudoClass checkmate = PseudoClass.getPseudoClass("checkmate");
        boardStateProperty.addListener(inv -> {
            String state = boardStateProperty.get();
            label.pseudoClassStateChanged(stalemate, false);
            label.pseudoClassStateChanged(checkmate, false);
            label.pseudoClassStateChanged(check, false);
            switch (state.toLowerCase()) {
                case "check":
                    Log.warn("BOARD STATE PROPERTY IS CHECK!!");
                    label.pseudoClassStateChanged(check, true);
                    break;
                case "stalemate":
                    label.pseudoClassStateChanged(stalemate, true);
                    break;
                case "checkmate":
                    label.pseudoClassStateChanged(checkmate, true);
                    break;
            }
        });
    }


    private Node makeTurnCounter(IntegerProperty turnCountProperty) {
        Label label = new Label();
        label.textProperty().bind(turnCountProperty.asString());
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
        return rootPane;
    }


    /**
     * <b>The entry point for the chess game.</b>
     * This is where a user is directed after pressing a play button.
     * <p>
     * Due to threading, this likely happens during the `manageLayoutAnimation()`
     * in RootWrapperBuilder.
     *
     *
     */
    @Override
    public void startup() {
        Task<Void> loadBoardModel = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Log.debug("GameLayoutBuilder", "Creating ReactiveChessContainer off the FXAT...");
                boardContainer = new ReactiveChessContainer(new GameStateModel(chessConfig), chessConfig.getGameBoard());
                Log.debug("Finished making ReactiveChessContainer, calling start method...");
                boardContainer.gameStart();
                Log.debug("Finished.");
                return null;
            }
        };
        loadBoardModel.setOnSucceeded(event -> {
            Log.debug("GameLayoutBuilder", "Successfully created backend board.");
            ChessBoardBuilder boardBuilder = (new ChessBoardBuilder(boardContainer, chessConfig, layoutManager));
            boardUIModel = boardBuilder.getBoardUIModel();
            rootPane.setCenter(screenCentreHBox(boardBuilder.build(), makeMoveTableSideBar(
                    new MoveTable(boardContainer.getMoveTableData(), makeGameButton("<"))
            )));
            rootPane.setTop(makeGameTopWrapper(boardContainer.fullmoveCounterProperty(), boardContainer.boardStateProperty()));
            rootPane.setBottom(makeTurnLabel(boardContainer.currentTeamStringProperty()));
            boardUIModel.gameStart();
            super.startup();
        });
        Thread thread = new Thread(loadBoardModel);
        thread.start();
    }

    private Node screenCentreHBox(Node board, Node moveTable) {
        HBox hBox = new HBox(board, moveTable);
        // Otherwise board is tiny!
        HBox.setHgrow(board, Priority.ALWAYS);

        hBox.getStyleClass().add("game-screen-centre");
        return hBox;
    }

    private Node makeMoveTableSideBar(MoveTable moveTable) {
        // Styling to make the button work better.
        return moveTable;
    }


    private Node makeGameTopWrapper(IntegerProperty fullmoveCounterProperty, StringProperty boardStateProperty) {
        BorderPane pane = new BorderPane();

        pane.setLeft(makeTopLeftHBox());
        pane.setRight(makeUndoButton());
        pane.setCenter(makeTurnCounterHBox(fullmoveCounterProperty, boardStateProperty));

        pane.getStyleClass().add("game-top-wrapper");
        return pane;
    }

    private HBox makeTopLeftHBox() {
        Button returnButton = returnButton("Exit", endAndReturnAction());
        returnButton.setFocusTraversable(false);
        Button settingsButton = SettingsLayoutUtil.makeIconButton(appSettings, layoutManager.getPopupManager());
        settingsButton.setFocusTraversable(false);

        HBox hBox = new HBox(returnButton, settingsButton);
        hBox.getStyleClass().add("return-vbox");
        return hBox;
    }

    private Node makeThemeDropDown() {
        MenuButton dropdown = new ThemeDropDown(appSettings).build();
        dropdown.setFocusTraversable(false);
        return dropdown;
    }


    private Node makeUndoButton() {
        Button button = makeGameButton("Undo", () -> {
            if (boardContainer == null) return;
            boardUIModel.undoLastMoveIfPossible();
        });
        // Vbox does not take up horizontal space on layout
        button.getStyleClass().add("undo-button");
        VBox box = new VBox(button);
        box.getStyleClass().add("undo-button");
        return box;
    }


    private Region makeTurnLabel(StringProperty currentTeamStringProperty) {

        Label label = new Label();
        label.textProperty().bind(Bindings.concat(currentTeamStringProperty, "'s turn"));
        label.getStyleClass().add("current-turn-label");

        HBox hBox = new HBox(label);
        hBox.getStyleClass().add("current-turn-label");
        return hBox;
    }

    public void terminate() {
        Task<Void> terminate = new Task<>() {
            @Override
            protected Void call() throws Exception {
                boardContainer.terminate();
                return null;
            }
        };
        Thread thread = new Thread(terminate);
        thread.start();
    }

}

