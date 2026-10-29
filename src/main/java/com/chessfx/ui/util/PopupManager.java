package com.chessfx.ui.util;

import com.chessfx.chessconfig.AppSettings;
import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.board.Team;
import com.chessfx.log.Log;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.menu.GamePopup;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

import java.util.List;


public class PopupManager {

    private final ObservableList<Node> rootChildren;
    private final ReadOnlyDoubleProperty rootHeightProperty;
    private final ReadOnlyDoubleProperty rootWidthProperty;

    // For stack logic
    private int popupIndex = 0;
    boolean locked = false;

    public PopupManager(Pane popupLayer, ReadOnlyDoubleProperty rootHeightProperty, ReadOnlyDoubleProperty rootWidthProperty) {
        this.rootChildren = popupLayer.getChildren();
        this.rootHeightProperty = rootHeightProperty;
        this.rootWidthProperty = rootWidthProperty;
    }

    public ObservableList<Node> getRootChildren() {
        return rootChildren;
    }

    public void askGameTypeAndWait(Runnable changeLayoutToGame, ChessConfigModel chessConfig) {
        // GamePopup class destroys itself upon completion, and switches layout if necessary.
        if (locked) return;
        Log.info("PopupManager", "Made new popup.");
        GamePopup popup = new GamePopup(changeLayoutToGame,
                this::pop, chessConfig);
        rescale(0.3, popup);
        push(popup);
        toggleLock();
    }

    private void toggleLock() {
        locked = !locked;
    }

    private void pop() {
        // Prevent removal of the appLevelBorderPane.
        if (popupIndex == 0)
            return;

        rootChildren.remove(popupIndex);
        popupIndex--;
    }

    private void push(GamePopup popup) {
        popupIndex++;
        rootChildren.add(popupIndex, popup);
    }

    private void push(Node node) {
        popupIndex++;
        rootChildren.add(popupIndex, node);
    }

    public Node closeButton() {
        Button button = new Button("Close");
        HBox hBox = new HBox(button);
        hBox.getStyleClass().add("close-popup-button");
        button.setOnAction(event -> {
            pop();
            toggleLock();
        });
        return hBox;
    }

    public void makeSettingsPopup(AppSettings appSettings) {
        if (locked) {
            pop();
            toggleLock();
            return;
        }
        BorderPane popup = new BorderPane();
        popup.getStyleClass().add("settings-popup");
        popup.setTop(closeButton());
        popup.setCenter(SettingsLayoutUtil.makeSettingsVbox(appSettings));
        rescale(0.5, popup);
        push(popup);
        toggleLock();
    }

    private void rescale(double scale, Region popup) {
        popup.maxHeightProperty().bind(rootHeightProperty.multiply(scale));
        popup.maxWidthProperty().bind(rootWidthProperty.multiply(scale));
    }

    public void promotionPopup(PieceSpriteFactory pieceSpriteFactory, Team promotingTeam) {
        if (locked) {
            Log.info("Cannot close promotion popup without promoting");
        }
        Region popup = new PromotionPopupBuilder(pieceSpriteFactory,
                List.of("queen", "rook", "knight", "bishop"), promotingTeam).build();

        push(popup);
        toggleLock();
    }

}
