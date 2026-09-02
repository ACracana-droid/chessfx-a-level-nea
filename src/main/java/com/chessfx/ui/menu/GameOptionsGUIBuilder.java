package com.chessfx.ui.menu;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.awt.*;


public class GameOptionsGUIBuilder implements Builder<Region> {
    BorderPane rootPane;

    public GameOptionsGUIBuilder(Runnable changeLayoutToMenu) {
        rootPane = new BorderPane();
        rootPane.setTop(new ReturnButton(changeLayoutToMenu, "Return to main menu").getAsVBox());
        rootPane.setCenter(makeOptionsVBox());
    }

    private Node makeOptionsVBox() {
        VBox vBox = new VBox();
        return vBox;
    }

    @Override
    public Region build() {
        return rootPane;
    }


    public Pane[] panes = new Pane[40];
    GridPane gridPane;

    // Source - https://stackoverflow.com/a/35646599
// Posted by Oliver Jan Krylow
// Retrieved 2026-08-19, License - CC BY-SA 3.0

    private Node layoutNodes() {
        gridPane = new GridPane();
        final RowConstraints rowsEdge = new RowConstraints();
        rowsEdge.setPercentHeight(14);
        final RowConstraints rowsMid = new RowConstraints();
        rowsMid.setPercentHeight(8);

        final ColumnConstraints colEdge = new ColumnConstraints();
        colEdge.setPercentWidth(14);

        final ColumnConstraints colMid = new ColumnConstraints();
        colMid.setPercentWidth(8);

        gridPane.getColumnConstraints().addAll(colEdge, colMid, colMid, colMid, colMid, colMid, colMid, colMid, colMid, colMid, colEdge);
        gridPane.getRowConstraints().addAll(rowsEdge, rowsMid, rowsMid, rowsMid, rowsMid, rowsMid, rowsMid, rowsMid, rowsMid, rowsMid, rowsEdge);

        final StackPane imagePane = new StackPane();
        // mad logic
        gridPane.add(imagePane, 1, 1, 9, 9);

        final DoubleBinding multipliedHeight = gridPane.heightProperty().multiply(0.72);
        final DoubleBinding multipliedWidth = gridPane.widthProperty().multiply(0.72);

        imagePane.maxHeightProperty().bind(multipliedHeight);
        imagePane.maxWidthProperty().bind(multipliedWidth);
        imagePane.minHeightProperty().bind(multipliedHeight);
        imagePane.minWidthProperty().bind(multipliedWidth);
        imagePane.prefHeightProperty().bind(multipliedHeight);
        imagePane.prefWidthProperty().bind(multipliedWidth);

        final ImageView imageView =
                new ImageView("http://1.bp.blogspot.com/-Wjc79oqi1y0/VHitLAU44BI/AAAAAAAAG80/0UZ9n2JmvEo/s1600/Logo%2BMonopoly.png");
        imageView.setPreserveRatio(true);
        imageView.fitWidthProperty().bind(imagePane.widthProperty().divide(2));

        imagePane.setStyle("-fx-background-color: red;");
        imagePane.getChildren().add(imageView);

//        gridPane.setGridLinesVisible(true);
        return gridPane;
    }


}
