package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.ui.game.board.ChessBoardOrnamentBuilder;
import com.chessfx.ui.util.LayoutManager;
import com.chessfx.ui.util.SwitchButton;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;

import static com.chessfx.ui.util.LayoutManager.Layout.*;
import static com.chessfx.ui.util.LayoutManager.Layout.GAME_OPTIONS;
import static com.chessfx.ui.util.LayoutManager.Layout.MENU;

public class MenuLayoutBuilder extends LayoutBuilder {

    BorderPane rootPane;
    ChessConfigModel chessConfig;

    public MenuLayoutBuilder(LayoutManager layoutManager,
                             ChessConfigModel chessConfig) {
        super(chessConfig, layoutManager, MENU);

        this.chessConfig = chessConfig;
    }

    private Node createOptionsVBox() {
        VBox vBox = new VBox(
                makeDefaultGameButton(),
                makeWildCardButton(),
                makeSettingsButton(),
                makeMoreOptionsButton());
        vBox.getStyleClass().add("options-vbox");
        return vBox;
    }


    private Button makeDefaultGameButton() {
        Button button = makeQuickGameButton("defaultPvP.properties");
        button.setText("Default Game");
        return button;
    }

    private Node makeBoardOrnament() {
        Pane chessBoard = new ChessBoardOrnamentBuilder().build();
        return chessBoard;
    }

    private Node makeSettingsButton() {
        Button button = makeMenuButton("Settings");
        button.setOnAction(gameButtonAction(() -> layoutManager.startup(SETTINGS)));
        return button;
    }

    private Button makeMenuButton(String text) {
        return applyDisableSync(new Button(text));
    }

    private Node makeMoreOptionsButton() {
        Button button = makeMenuButton("See more");
        button.setOnAction(gameButtonAction(() -> layoutManager.startup(GAME_OPTIONS)));
        return button;
    }


    private Node makeWildCardButton() {
        Button button = makeQuickGameButton("reallyBigBasicChess.properties");
        button.setText("Wildcard 2 player");
        return button;
    }


    @Override
    public Region build() {
        rootPane = new BorderPane(createOptionsVBox());
        rootPane.setTop(makeBoardOrnament());
        return rootPane;
    }

    @Override
    public void startup() {
        super.startup();
    }
}
