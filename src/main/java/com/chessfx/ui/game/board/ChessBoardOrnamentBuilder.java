package com.chessfx.ui.game.board;

import javafx.scene.Node;
import javafx.scene.text.Text;


public class ChessBoardOrnamentBuilder extends ChessBoardBuilder {

    public ChessBoardOrnamentBuilder() {
        super();
        view.setRotate(45.0);
        view.getStyleClass().add("title-ornament");
        view.getChildren().add(makeTitleLabel());
    }

    private Node makeTitleLabel() {
        Text label = new Text("ChessFX");
        label.getStyleClass().add("title-label");
        label.setRotate(-45.0);
        return label;
    }
}
