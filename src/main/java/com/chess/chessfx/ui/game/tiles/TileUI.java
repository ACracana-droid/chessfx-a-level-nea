package com.chess.chessfx.ui.game.tiles;

import com.chess.chessfx.engine.tiles.BarrierTile;
import com.chess.chessfx.engine.tiles.TileParent;
import com.chess.chessfx.engine.tiles.VoidTile;
import com.chess.chessfx.ui.game.BoardUIModel;
import com.chess.chessfx.ui.game.pieces.PieceSpriteFactory;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;


public class TileUI extends StackPane {

    TileParent tile;
    StringProperty uiState = new SimpleStringProperty("");
    BoardUIModel uiModel;

    public TileUI(TileParent tile, TileColour tileColour, NumberBinding tileSizeBinding,
                  PieceSpriteFactory pieceSpriteFactory, BoardUIModel uiModel, int i, int j) {
        super();
        this.tile = tile;
        this.uiModel = uiModel;
        this.setMinSize(30, 30);
        this.prefHeightProperty().bind(tileSizeBinding);
        this.prefWidthProperty().bind(tileSizeBinding);

        // Styling for UI
        String colourStyleSheet = "";
        switch (tileColour) {
            case LIGHT -> colourStyleSheet = "light-tile";
            case DARK -> colourStyleSheet = "dark-tile";
        }
        getStyleClass().add(colourStyleSheet);

        if (tile != null)
            tile.style(this);
        else {
            System.out.println("TileUI: This TileParent is null!");
            getStyleClass().add("null-tile"); // for debugging
        }

        /// temporarily show array numberings.
        Label numbering = new Label(i + ", " + j);
        numbering.setVisible(true);
        this.getChildren().add(numbering);

        /// Tile should never ever be null, but (for now) maybe it is.
        if (tile != null && tile.hasPiece())
            showPiece(pieceSpriteFactory);

        if (!(tile instanceof BarrierTile)) {
            setUpPseudoClasses();
            this.setOnMouseClicked(event -> handleTileClick());
        }

    }

    public TileUI(VoidTile tile) {
        this.tile = tile;
    }

    private void setUpPseudoClasses() {
        PseudoClass active = PseudoClass.getPseudoClass("active");
        PseudoClass validMove = PseudoClass.getPseudoClass("valid-move");
        PseudoClass capture = PseudoClass.getPseudoClass("capture");
        uiState.addListener(inv -> {
            String state = this.uiState.get();
            this.pseudoClassStateChanged(validMove, false);
            this.pseudoClassStateChanged(capture, false);
            this.pseudoClassStateChanged(active, false);
            switch (state) {
                case "active":
                    this.pseudoClassStateChanged(active, true);
                    break;
                case "valid-move":
                    this.pseudoClassStateChanged(validMove, true);
                    break;
                case "capture":
                    this.pseudoClassStateChanged(capture, true);
                    break;
            }
            System.out.println("Pseudoclass active: " + state);
        });
    }

    private void handleTileClick() {
        uiModel.handleTileClick(this, tile);
    }

    public void setActive() {
        uiState.set("active");
    }

    public boolean isMoveHighlighted() {
        return uiState.get().equals("valid-move");
    }

    private void showTileRejectionEffect() {
    }

    private void showPieceRejectionEffect() {
    }

    public void setDefaultState() {
        uiState.set("");
    }

    private void showPiece(PieceSpriteFactory pieceSpriteFactory) {
        this.getChildren().add(pieceSpriteFactory.makePieceSprite(tile));
    }

    public TileParent getTile() {
        return tile;
    }

    public void setMoveHighlight(boolean isCapture) {
        if (isCapture) uiState.set("capture");
        else uiState.set("valid-move");
    }
}
