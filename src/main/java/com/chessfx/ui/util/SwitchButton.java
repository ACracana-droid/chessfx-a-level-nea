package com.chessfx.ui.util;

import javafx.css.PseudoClass;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;


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

//        this.setStyle("-fx-border-width: " + OFFSET + ";"
//                + "-fx-border-radius: " + CIRCLE_RADIUS + ";"
//                + "-fx-background-radius: " + CIRCLE_RADIUS + ";"
//        );

        circle = makeCircleButton();
        this.getChildren().addAll(circle);
        this.getStyleClass().add("button-switch");


//        Border
//        this.setBorder(new Border(new BorderStroke(Color.BEIGE, BorderStrokeStyle.SOLID, new CornerRadii(CIRCLE_RADIUS), new BorderWidths(OFFSET * 10))));
//        this.setBackground(new Background(new BackgroundFill(Color.BLUE, new CornerRadii(CIRCLE_RADIUS), new Insets(0.0))));
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
