package com.chessfx.ui.util;

import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Move;
import com.chessfx.ui.game.board.BoardUIManager;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

import java.util.List;

public class PromotionPopupBuilder implements Builder<Region> {


    private final PieceSpriteFactory pieceSpriteFactory;
    private final List<String> pieceNames;
    private final Team promotingTeam;
    private final PopupManager popupManager;
    private final BoardUIManager boardUIManager;
    private final Move move;
    private boolean pieceSelected = false;

    public PromotionPopupBuilder(PieceSpriteFactory pieceSpriteFactory,
                                 List<String> pieceNames,
                                 Team promotingTeam,
                                 PopupManager popupManager, BoardUIManager boardUIManager, Move move) {
        this.pieceSpriteFactory = pieceSpriteFactory;
        this.pieceNames = pieceNames;
        this.promotingTeam = promotingTeam;
        this.popupManager = popupManager;
        this.boardUIManager = boardUIManager;
        this.move = move;
    }

    @Override
    public Region build() {
        return makePane();
    }

    private Region makePane() {
        Pane pane = new StackPane(makeHBox());
        pane.getStyleClass().add("settings-popup");
        return pane;
    }

    private Region makeHBox() {
        HBox hBox = new HBox();
        for (String name : pieceNames) {
            hBox.getChildren().add(makePieceSprite(name));
        }
        hBox.getStyleClass().add("promotion-popup");
        return hBox;
    }

    private Node makePieceSprite(String name) {
        Region sprite = pieceSpriteFactory.makePieceSprite(name, promotingTeam);
        sprite.setMaxSize(100, 100);
        sprite.setOnMouseClicked(event -> {
            if (pieceSelected) return;
            pieceSelected = true;
            boardUIManager.finishPromotionMove(move, name);

            popupManager.pop();
            popupManager.toggleLock();
        });
        return sprite;
    }
}
