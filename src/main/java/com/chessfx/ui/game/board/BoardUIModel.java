package com.chessfx.ui.game.board;

import com.chessfx.engine.board.BoardContainer;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.DevTileUI;
import com.chessfx.ui.game.tiles.TileUI;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class BoardUIModel {

    // Selection
    TileUI selectedTile; // there can only ever be 1.
    List<TileUI> prospectiveMoveTiles;

    final TileUI DEV_TILE;
    BoardContainer boardContainer;
    private final PieceSpriteFactory pieceSpriteFactory;
    TileUI[][] boardUiTiles;

    public BoardUIModel(BoardContainer boardContainer, TileUI[][] boardUiTiles, PieceSpriteFactory pieceSpriteFactory) {
        this.boardContainer = boardContainer;
        this.pieceSpriteFactory = pieceSpriteFactory;
        DEV_TILE = new DevTileUI();
        this.boardUiTiles = boardUiTiles;
        selectedTile = DEV_TILE;
        prospectiveMoveTiles = new ArrayList<>();
    }

    private void select(TileUI tileUI) {
        selectedTile.setDefaultState();

        tileUI.setActive();
        selectedTile = tileUI;
        Log.debug("BoardUIModel", "Selected Piece");
        showMoves(getMovesToShow(tileUI));
    }

    private List<Move> getMovesToShow(TileUI tileUI) {
        List<Move> unvalidatedMoves = tileUI.getTile().getPiece().moves(boardContainer);
        return boardContainer.getValidatedMoves(unvalidatedMoves);
    }

    private void showMoves(List<Move> moves) {
        Log.info("BoardUIModel","Showing moves: ");
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
     * The central game interaction layer through which the user interacts with the board.
     * Performs various actions depending on the status of the tile clicked and the surrounding context.
     *
     * @param tileUI          the graphic clicked
     * @param tile            the backend tile
     * @param prospectiveMove the move proposed by click if there is one
     */
    // TODO: Document new version
    public void handleTileClick(TileUI tileUI, TileBase tile, Move prospectiveMove) {
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
                select(tileUI);
            }
        } else if (selectedTile.equals(tileUI)) {
            Log.info("Cancelling move..");
            deselect();
        } else { //// cancel move
            if (tile.hasPieceMatchingTeam(boardContainer.getCurrentTeam())) { //// cancel, select new
                Log.info("Cancelling move and selecting new piece");
                deselect();
                select(tileUI);
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

        Node sprite = from.removeExistingPieceSprite();
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

        Node movedSprite = from.removePieceSprite();
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
