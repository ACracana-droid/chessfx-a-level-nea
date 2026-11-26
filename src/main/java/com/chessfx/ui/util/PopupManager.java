package com.chessfx.ui.util;

import com.chessfx.chessconfig.AppSettings;
import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Move;
import com.chessfx.log.Log;
import com.chessfx.ui.game.board.BoardUIManager;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import org.girod.javafx.svgimage.SVGLoader;

import java.util.List;
import java.util.Objects;


public class PopupManager {

    private final ObservableList<Node> rootChildren;
    private final ReadOnlyDoubleProperty rootHeightProperty;
    private final ReadOnlyDoubleProperty rootWidthProperty;

    // For stack logic
    private int popupIndex = 0;
    private boolean locked = false;
    private LayoutManager layoutManager;

    public PopupManager(Pane popupLayer, ReadOnlyDoubleProperty rootHeightProperty, ReadOnlyDoubleProperty rootWidthProperty) {
        this.rootChildren = popupLayer.getChildren();
        this.rootHeightProperty = rootHeightProperty;
        this.rootWidthProperty = rootWidthProperty;
    }

    public ObservableList<Node> getRootChildren() {
        return rootChildren;
    }


    void toggleLock() {
        locked = !locked;
    }

    void pop() {
        // Prevent removal of the appLevelBorderPane.
        if (popupIndex == 0)
            return;

        rootChildren.remove(popupIndex);
        popupIndex--;
    }


    void push(Node node) {
        popupIndex++;
        rootChildren.add(popupIndex, node);
    }

    public Node closeButtonRegion() {
        HBox hBox = new HBox(closeButton());
        hBox.getStyleClass().addAll("close-popup-button");
        return hBox;
    }

    public Button closeButton() {
        Button button = new Button();
        button.setGraphic(SVGLoader.load(Objects.requireNonNull(getClass().getResource("close.svg"))));

        button.getStyleClass().addAll("icon-button");
        button.setOnAction(event -> {
            pop();
            toggleLock();
            layoutManager.getCurrentLayoutBuilder().enableButtons();
        });
        return button;
    }

    public void makeSettingsPopup(AppSettings appSettings) {
        if (locked) {
            pop();
            toggleLock();
            return;
        }
        BorderPane popup = makePopup();
        popup.setCenter(SettingsLayoutUtil.makeSettingsVbox(appSettings));
        push(popup);
        toggleLock();
    }

    private BorderPane makePopup() {
        BorderPane popup = new BorderPane();
        popup.getStyleClass().add("popup");
        popup.setTop(closeButtonRegion());
        rescale(0.5, popup);
        return popup;
    }

    private void rescale(double scale, Region popup) {
        popup.maxHeightProperty().bind(rootHeightProperty.multiply(scale));
        popup.maxWidthProperty().bind(rootWidthProperty.multiply(scale));
    }

    public void promotionPopup(PieceSpriteFactory pieceSpriteFactory, Team promotingTeam, List<String> promotablePiecesList, BoardUIManager boardUIManager, Move destinationTile) {
        if (locked) {
            Log.info("Cannot close promotion popup without promoting");
        }
        Region popup = new PromotionPopupBuilder(pieceSpriteFactory,
                promotablePiecesList, promotingTeam, this, boardUIManager, destinationTile).build();
        rescale(0.5, popup);

        push(popup);
        toggleLock();
    }

    public void gameConfigurePopup(boolean isVsBot, LayoutManager layoutManager, ChessConfigModel chessConfig) {
//     todo:  1. Read objectives. Make Game config popup.
        if (locked) {
            pop();
            toggleLock();
            layoutManager.getCurrentLayoutBuilder().enableButtons();
        }
        layoutManager.getCurrentLayoutBuilder().disableButtons();
        BorderPane popup = new GameConfigurationPopup(isVsBot, layoutManager, chessConfig);
        rescale(0.8, popup);


        push(popup);
        toggleLock();
    }


    public void setLayoutManager(LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }
}
