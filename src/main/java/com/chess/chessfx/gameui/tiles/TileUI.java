package com.chess.chessfx.gameui.tiles;

import com.chess.chessfx.engine.tiles.TileParent;
import com.chess.chessfx.gameui.pieces.PieceSpriteFactory;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.StackPane;


public class TileUI extends StackPane {

    TileParent tile;

    public TileUI(TileParent tile, TileColour tileColour, NumberBinding tileSizeBinding, PieceSpriteFactory pieceSpriteFactory) {
        super();
        this.tile = tile;
        this.setMinSize(30, 30);
        this.prefHeightProperty().bind(tileSizeBinding);
        this.prefWidthProperty().bind(tileSizeBinding);

        // Styling for UI
        String stylesheet = "";
        switch (tileColour) {
            case LIGHT -> stylesheet = "light-tile";
            case DARK -> stylesheet = "dark-tile";
        }
        getStyleClass().add(stylesheet);
//        assert tile != null;

        if (tile != null)
            tile.style(this);
        else {
            System.out.println("TileUI: This TileParent is null!");
            getStyleClass().add("null-tile"); // for debugging
        }
        /// Tile should never ever be null, but (for now) maybe it is.
        if (tile != null && tile.hasPiece())
            showPiece(pieceSpriteFactory);

    }

    private void showPiece(PieceSpriteFactory pieceSpriteFactory) {
        this.getChildren().add(pieceSpriteFactory.makePieceSprite(tile));

    }
}
