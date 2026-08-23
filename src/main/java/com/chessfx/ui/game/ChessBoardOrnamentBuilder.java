package com.chessfx.ui.game;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.text.Text;


public class ChessBoardOrnamentBuilder extends ChessBoardBuilder {

    public ChessBoardOrnamentBuilder() {
        super();
        container.setRotate(45.0);
        container.getStyleClass().add("title-ornament");
        container.getChildren().add(makeTitleLabel());
    }

    private Node makeTitleLabel() {
        Text label = new Text("ChessFX");
        label.getStyleClass().add("title-label");
        label.setRotate(-45.0);
        return label;
    }
}
