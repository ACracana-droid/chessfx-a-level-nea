package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Builder;

import static com.chessfx.ui.menu.LayoutManager.Layout.GAME_OPTIONS;


public class AllGameOptionsLayoutBuilder extends LayoutBuilder {


    BorderPane rootPane;

    public AllGameOptionsLayoutBuilder(LayoutManager layoutManager, ChessConfigModel chessConfig) {
        super(chessConfig, layoutManager, GAME_OPTIONS);
        rootPane = new BorderPane();
        rootPane.setTop(returnButtonVbox("Return to main menu",
                () -> layoutManager.startup(LayoutManager.Layout.MENU)));
        rootPane.setCenter(makeOptionsVBox());
    }

    private Node makeOptionsVBox() {
        VBox vBox = new VBox(makeCapablancaPlayButton());
        vBox.getStyleClass().add("options-vbox");
        return vBox;
    }

    private Button makeCapablancaPlayButton() {
//        Button button = (changeLayoutToGame,
//                (() -> chessConfig.setChessConfigFromPropertie
//                sResource("capablancaPvP.properties")));
        Button button = makeQuickGameButton("capablancaPvP.properties");
        button.setText("Capablanca PvP");
        return button;
    }

    @Override
    public void startup() {
        super.startup();
    }

    @Override
    public Region build() {
        return rootPane;
    }

}
