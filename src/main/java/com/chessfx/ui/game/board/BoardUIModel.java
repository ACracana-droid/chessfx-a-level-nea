package com.chessfx.ui.game.board;

import com.chessfx.engine.board.BoardContainer;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;
import com.chessfx.ui.game.DragHandler;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.DevTileUI;
import com.chessfx.ui.game.tiles.TileUI;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class BoardUIModel {

    // Selection
    private TileUI selectedTile; // there can only ever be 1.
    private final List<TileUI> prospectiveMoveTiles;

    // Immutable:
    private final TileUI DEV_TILE;
    private final DragHandler dragHandler;
    private final BoardContainer boardContainer;
    private final PieceSpriteFactory pieceSpriteFactory;
    private final TileUI[][] boardUiTiles;


    // CONFIG
    private boolean isDragEnabled = true;

    public BoardUIModel(DragHandler dragHandler, BoardContainer boardContainer, TileUI[][] boardUiTiles,
                        PieceSpriteFactory pieceSpriteFactory) {

        this.dragHandler = dragHandler;
        dragHandler.addEndDragHandler(this::endDrag);
        this.boardContainer = boardContainer;
        this.pieceSpriteFactory = pieceSpriteFactory;
        this.boardUiTiles = boardUiTiles;

        this.prospectiveMoveTiles = new ArrayList<>();
        this.DEV_TILE = new DevTileUI();
        this.selectedTile = DEV_TILE;
    }


    private void select(TileUI tileUI, MouseEvent event) {
        selectedTile.setDefaultState();

        tileUI.setActive();
        selectedTile = tileUI;
        Log.debug("BoardUIModel", "Selected Piece");
        showMoves(getMovesToShow(tileUI));
        if (isDragEnabled) {
            dragHandler.startDrag(tileUI, event);
        }
    }

    public void endDrag(MouseEvent event) {
        TileUI picked = dragHandler.endDrag(event);
        if (picked != null && prospectiveMoveTiles.contains(picked)) {
            /// do move

            doMove(selectedTile, picked);
            // reset board UI state.
            deselect();
            return;
        }
        /// else, abort move

    }

    private List<Move> getMovesToShow(TileUI tileUI) {
        List<Move> unvalidatedMoves = tileUI.getTile().getPiece().moves(boardContainer);
        return boardContainer.getValidatedMoves(unvalidatedMoves);
    }

    private void showMoves(List<Move> moves) {
        Log.info("BoardUIModel", "Showing moves: ");
        prospectiveMoveTiles.clear();
        for (Move move : moves) {
            TileUI tileUI = boardUiTiles[move.getEndY()][move.getEndX()];
            prospectiveMoveTiles.add(tileUI);
            tileUI.setProspectiveMove(move);
//            System.out.println("Tile at " + move.getEndY() + " ," + move.getEndX() + " is valid move");
        }
    }

    private void deselect() {
        selectedTile.setDefaultState();
        for (TileUI tileUI : prospectiveMoveTiles) {
            tileUI.setDefaultState();
        }
        DEV_TILE.setActive();
        selectedTile = DEV_TILE;
    }

    /**
     * The central game interaction layer interacts with the board.
     * Performs various actions depending on the status of the tile clicked and the surrounding context.
     *
     * @param tileUI          the graphic pressed
     * @param tile            the backend tile
     * @param prospectiveMove the move proposed by the selected tile if there is one
     * @param event           the mouse event for the press
     */
    // TODO: Document new version
    public void handleTilePressed(TileUI tileUI, TileBase tile,
                                  Move prospectiveMove, MouseEvent event) {
        Log.debug("Clicked tile: " + tileUI);
        Log.debug("Selected tile: " + selectedTile);
        if (prospectiveMove != null) { //// do move
            Log.debug("Prospective move: " + prospectiveMove);
            Log.info("Doing move...");
            doMove(selectedTile, tileUI);
            // reset board UI state.
            deselect();
            return;
        }

        if (selectedTile.equals(DEV_TILE)) {
            if (tile.hasPieceMatchingTeam(boardContainer.getCurrentTeam())) { //// initiate move.
                Log.info("Initiating move...");
                select(tileUI, event);
            }
        } else if (selectedTile.equals(tileUI)) {
            Log.info("Cancelling move..");
            deselect();
        } else { //// cancel move
            if (tile.hasPieceMatchingTeam(boardContainer.getCurrentTeam())) { //// cancel, select new
                Log.info("Cancelling move and selecting new piece");
                deselect();
                select(tileUI, event);
            } else { //// cancel
                Log.info("Cancelling move...");
                deselect();
            }
        }
    }

    /**
     * Upon correct user input, the game triggers UI updates and calls for the internal
     * data to be updated.
     *
     * @param from the TileUI which the move starts from.
     * @param to   the TileUI which was clicked.
     */
    private void doMove(TileUI from, TileUI to) {
        Move move = to.getProspectiveMove();

        Region sprite = from.removeExistingPieceSprite();
        to.removeExistingPieceSprite();

        boardContainer.doMove(move);

        to.showPieceSprite(sprite);
    }

    public void undoLastMoveIfPossible() {
        Move move = boardContainer.undoLastMove();
        if (move == null) {
            Log.warn("Undo move failed.");
            // TODO: Show that the undo failed in the UI state.
            return;
        }
        deselect();

        // Move back the last moved piece.
        TileUI from = getTileUI(move.getEndY(), move.getEndX());
        TileUI to = getTileUI(move.getStartY(), move.getStartX());

        Region movedSprite = from.removePieceSprite();
        to.showPieceSprite(movedSprite);

        // reinstate captured pieces affected by this move.
        if (move.isCapture()) {
            for (Piece p : move.getCaptures()) {
                getTileUI(p.getCoOrds()).showPieceSprite(pieceSpriteFactory.makePieceSprite(p));
            }
        }

    }

    private TileUI getTileUI(int[] coOrds) {
        return boardUiTiles[coOrds[0]][coOrds[1]];
    }

    public TileUI getTileUI(int y, int x) {
        return boardUiTiles[y][x];
    }


}
