package com.chessfx.ui.util;

import com.chessfx.engine.board.Team;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Builder;

import java.util.List;

public class PromotionPopupBuilder implements Builder<Region> {


    private final PieceSpriteFactory pieceSpriteFactory;
    private final List<String> pieceNames;
    private final Team promotingTeam;

    public PromotionPopupBuilder(PieceSpriteFactory pieceSpriteFactory, List<String> pieceNames, Team promotingTeam) {
        this.pieceSpriteFactory = pieceSpriteFactory;
        this.pieceNames = pieceNames;
        this.promotingTeam = promotingTeam;
    }

    @Override
    public Region build() {
        return makeHBox();
    }

    private Region makeHBox() {
        HBox hBox = new HBox();
        for (String name : pieceNames) {
            hBox.getChildren().add(pieceSpriteFactory.makePieceSprite(name, promotingTeam));
        }
        hBox.getStyleClass().add("promotion-popup");
        return hBox;
    }
}
