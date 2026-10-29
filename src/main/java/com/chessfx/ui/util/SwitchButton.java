package com.chessfx.ui.util;

import javafx.css.PseudoClass;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;


public class SwitchButton extends Pane {

    private final double RECTANGLE_HEIGHT = 30.0; // Size base.

    private final double OFFSET = RECTANGLE_HEIGHT * 0.5;

    private final double RECTANGLE_WIDTH = RECTANGLE_HEIGHT * 1.7;
    private final double CIRCLE_RADIUS = RECTANGLE_HEIGHT * 0.3;
    private final double CIRCLE_SIZE = RECTANGLE_HEIGHT * 0.75;


    Circle circle;
    boolean active;
    PseudoClass activeState = PseudoClass.getPseudoClass("active");


    public SwitchButton() {
        super();

        this.setMinHeight(RECTANGLE_HEIGHT);
        this.setMinWidth(RECTANGLE_WIDTH + OFFSET);
        this.setMaxHeight(RECTANGLE_HEIGHT);
        this.setMaxWidth(RECTANGLE_WIDTH + OFFSET);

        this.setStyle(
                "-fx-border-width: " + OFFSET * 0.1 + ";" +
                        "-fx-border-radius: " + RECTANGLE_HEIGHT + ";"
                        + "-fx-background-radius: " + RECTANGLE_HEIGHT + ";"
        );
        circle = makeCircleButton();

        this.getChildren().addAll(circle);
        this.getStyleClass().add("button-switch");

        this.setOnMouseClicked(event -> {
            toggleSwitch();
        });
        circle.setLayoutX(OFFSET);
    }

    private void toggleSwitch() {
        if (active) {
            deactivate();
        } else {
            activate();
        }
    }

    private void deactivate() {
        active = false;
        this.pseudoClassStateChanged(activeState, false);
        circle.setLayoutX(OFFSET);

    }

    private void activate() {
        active = true;
        this.pseudoClassStateChanged(activeState, true);
        circle.setLayoutX(RECTANGLE_WIDTH);
    }


    private Circle makeCircleButton() {
        Circle circle = new Circle();
        circle.getStyleClass().add("button-switch-circle");
        circle.setMouseTransparent(true);
        circle.setRadius(CIRCLE_RADIUS);

        circle.minWidth(CIRCLE_SIZE);
        circle.minHeight(CIRCLE_SIZE);

        circle.maxWidth(CIRCLE_SIZE);
        circle.maxHeight(CIRCLE_SIZE);

        circle.setLayoutY(RECTANGLE_HEIGHT / 2);
        return circle;
    }
}
