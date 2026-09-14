package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

public class PopupManager {

    private final ObservableList<Node> rootChildren;
    private final ReadOnlyDoubleProperty rootHeightProperty;
    private final ReadOnlyDoubleProperty rootWidthProperty;

    // For stack logic
    private int popupIndex = 0;

    public PopupManager(ObservableList<Node> rootChildren, ReadOnlyDoubleProperty rootHeightProperty, ReadOnlyDoubleProperty rootWidthProperty) {
        this.rootChildren = rootChildren;
        this.rootHeightProperty = rootHeightProperty;
        this.rootWidthProperty = rootWidthProperty;
    }

    public ObservableList<Node> getRootChildren() {
        return rootChildren;
    }

    public void askGameTypeAndWait(Runnable changeLayoutToGame, ChessConfigModel chessConfig) {
        // GamePopup class destroys itself upon completion, and switches layout if necessary.
        GamePopup popup = new GamePopup(changeLayoutToGame,
                this::pop, chessConfig);
        double size = 0.3;
        popup.maxHeightProperty().bind(rootHeightProperty.multiply(size));
        popup.maxWidthProperty().bind(rootWidthProperty.multiply(size));
        push(popup);
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

}
