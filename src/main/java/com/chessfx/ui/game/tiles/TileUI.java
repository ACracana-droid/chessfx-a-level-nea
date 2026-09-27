package com.chessfx.ui.game.tiles;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.tiles.BarrierTile;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.engine.tiles.VoidTile;
import com.chessfx.ui.game.board.BoardUIModel;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;


public class TileUI extends StackPane {

    TileBase tile;
    StringProperty uiState = new SimpleStringProperty("");
    BoardUIModel uiModel;
    Move prospectiveMove;
    boolean isShowingSprite;

    public TileUI(TileBase tile, TileColour tileColour, NumberBinding tileSizeBinding,
                  PieceSpriteFactory pieceSpriteFactory, BoardUIModel uiModel, int i, int j) {
        super();
        this.tile = tile;
        this.uiModel = uiModel;

        // Styling for UI
        getStyleClass().add(getColourStyleClass(tileColour));

        if (tile != null)
            tile.style(this);
        else {
            System.out.println("TileUI: This TileParent is null!");
            getStyleClass().add("null-tile"); // for debugging
        }

        /// temporarily show array numberings.
//        Label numbering = new Label(i + ", " + j);
//        numbering.setVisible(true);
//        this.getChildren().add(numbering);

        /// Tile should never ever be null, but (for now) maybe it is.
        if (tile != null && tile.hasPiece())
            showNewPiece(pieceSpriteFactory);

        if (!(tile instanceof BarrierTile)) {
            setUpPseudoClasses();
            this.setOnMouseClicked(event -> handleTileClick());
            this.setOnMouseDragged(event -> handleTileDrag(event));
        }

    }

    private void handleTileDrag(MouseEvent event) {
        if (isShowingSprite && tile.hasPiece()){

        }
    }

    private String getColourStyleClass(TileColour tileColour) {
        switch (tileColour) {
            case LIGHT -> {
                return "light-tile";
            }
            case DARK -> {
                return "dark-tile";
            }
        }
        return "";
    }

    public TileUI(VoidTile tile) {
        this.tile = tile;
    }

    public TileUI(TileColour tileColour, NumberBinding tileSize) {
        super();
        // Styling for UI
        this.getStyleClass().addAll(getColourStyleClass(tileColour), "chess-tile");
        this.setMinSize(30, 30);
        this.prefHeightProperty().bind(tileSize);
        this.prefWidthProperty().bind(tileSize);
        setUpPseudoClasses();

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
        });
    }

    private void handleTileClick() {
        uiModel.handleTileClick(this, tile, prospectiveMove);
    }

    public void setActive() {
        uiState.set("active");
    }

    public boolean isMoveHighlighted() {
        return uiState.get().equals("valid-move") || uiState.get().equals("capture");
    }

    private void showTileRejectionEffect() {
    }

    private void showPieceRejectionEffect() {
    }

    public void setDefaultState() {
        uiState.set("");
        prospectiveMove = null;
    }

    private void showNewPiece(PieceSpriteFactory pieceSpriteFactory) {
        this.getChildren().add(pieceSpriteFactory.makePieceSprite(tile));
        isShowingSprite = true;
    }

    public TileBase getTile() {
        return tile;
    }

    public void setMoveHighlight(boolean isCapture) {
        if (isCapture) uiState.set("capture");
        else uiState.set("valid-move");
    }

    public Node removePieceSprite() {
        isShowingSprite = false;
        return this.getChildren().removeLast();
    }

    public boolean hasPiece() {
        return tile.hasPiece();
    }

    public Node removeExistingPieceSprite() {
        if (isShowingSprite) {
            System.out.println("Piece sprite exists and is being removed.");
            isShowingSprite = false;
            return this.getChildren().removeLast();
        } else {
            System.out.println("Piece sprite doesn't exist.");
        }
        return null;
    }

    public void showPieceSprite(Node pieceSprite) {
        this.getChildren().add(pieceSprite);
        isShowingSprite = true;
    }

    public void setProspectiveMove(Move move) {
        prospectiveMove = move;
        this.setMoveHighlight(move.isCapture());
    }

    public Move getProspectiveMove() {
        return prospectiveMove;
    }

}
