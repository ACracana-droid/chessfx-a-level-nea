package com.chessfx.ui.game.tiles;

import com.chessfx.engine.board.BoardState;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.tiles.BarrierTile;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.engine.tiles.VoidTile;
import com.chessfx.log.Log;
import com.chessfx.ui.game.board.BoardUIModel;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;


public class TileUI extends StackPane {

    private TileBase tile;
    private final StringProperty uiState = new SimpleStringProperty("");
    private BoardUIModel uiModel;
    private Move prospectiveMove;

    private boolean isShowingSprite;
    private Region pieceSprite;

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
            Log.error("TileUI", "This TileParent is null!");
            getStyleClass().add("null-tile"); // for debugging
        }

        /// show array numbering (ADD SETTING)
        Label numbering = new Label(i + ", " + j);
        numbering.setVisible(true);
        this.getChildren().add(numbering);

        /// Tile should never ever be null, but (for now) maybe it is.
        if (tile != null && tile.hasPiece())
            showNewPiece(pieceSpriteFactory);

        if (!(tile instanceof BarrierTile)) {
            setUpPseudoClasses();
            this.setOnMousePressed(event -> handleTilePressed(event));
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


    private void handleTilePressed(MouseEvent event) {
        uiModel.handleTilePressed(this, tile, prospectiveMove, event);
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
        pieceSprite = pieceSpriteFactory.makePieceSprite(tile);
        this.getChildren().add(pieceSprite);
        isShowingSprite = true;
    }

    public TileBase getTile() {
        return tile;
    }

    public void setMoveHighlight(boolean isCapture) {
        if (isCapture) uiState.set("capture");
        else uiState.set("valid-move");
    }

    public Region removePieceSprite() {
        isShowingSprite = false;
        this.getChildren().remove(pieceSprite);

        Region temp = pieceSprite;
        pieceSprite = null;
        return temp;
    }


    public Region removeExistingPieceSprite() {
        if (isShowingSprite) {
            System.out.println("Piece sprite exists and is being removed.");
            return removePieceSprite();
        } else {
            System.out.println("Piece sprite doesn't exist.");
        }
        return null;
    }

    public void showPieceSprite(Region pieceSprite) {
        this.pieceSprite = pieceSprite;
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

    public Region getPieceSprite() {
        return pieceSprite;
    }

    public Region swapSprites(Region swapSprite) {
        Region temp = pieceSprite;
        this.getChildren().remove(pieceSprite);
        this.getChildren().add(swapSprite);
        pieceSprite = swapSprite;
        return temp;
    }

    public void setQuasiHover(boolean b) {
        PseudoClass quasiHover = PseudoClass.getPseudoClass("hover");
        this.pseudoClassStateChanged(quasiHover, b);
    }

    public void setThreatened(boolean b) {
        PseudoClass threat = PseudoClass.getPseudoClass("threat");
        this.pseudoClassStateChanged(threat, b);
    }

    public void check(boolean b) {
        PseudoClass check = PseudoClass.getPseudoClass("check");
        this.pseudoClassStateChanged(check, b);
    }
    private void checkmate(boolean b) {
        PseudoClass checkmate = PseudoClass.getPseudoClass("checkmate");
        this.pseudoClassStateChanged(checkmate, b);
    }

    public void showBoardStateEffect(BoardState boardState) {
        check(false);
        checkmate(false);
        stale(false);
        switch (boardState) {
            case CHECK -> check(true);
            case CHECKMATE -> checkmate(true);
            case STALEMATE -> stale(true);
        }
    }

    private void stale(boolean b) {
        PseudoClass stale = PseudoClass.getPseudoClass("stalemate");
        this.pseudoClassStateChanged(stale, b);
    }


}
