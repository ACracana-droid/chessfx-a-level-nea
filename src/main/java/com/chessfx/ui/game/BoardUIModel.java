package com.chessfx.ui.game;

import com.chessfx.engine.BoardContainer;
import com.chessfx.engine.Move;
import com.chessfx.engine.tiles.TileParent;
import com.chessfx.engine.tiles.VoidTile;
import com.chessfx.ui.game.tiles.TileUI;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class BoardUIModel {

    // Selection
    TileUI selectedTile; // there can only ever be 1.
    List<TileUI> prospectiveMoveTiles;

    final TileUI DEV_TILE;
    BoardContainer boardModel;
    TileUI[][] boardUiTiles;

    public BoardUIModel(BoardContainer boardModel, TileUI[][] boardUiTiles) {
        this.boardModel = boardModel;
        DEV_TILE = new TileUI(new VoidTile());
        this.boardUiTiles = boardUiTiles;
        selectedTile = DEV_TILE;
        prospectiveMoveTiles = new ArrayList<>();
    }

    private void select(TileUI tileUI) {
        selectedTile.setDefaultState();

        tileUI.setActive();
        selectedTile = tileUI;
        System.out.println("BoardUIModel: Selected Piece");
        showMoves(getMovesToShow(tileUI));
    }

    private List<Move> getMovesToShow(TileUI tileUI) {
        List<Move> unvalidatedMoves = tileUI.getTile().getPiece().moves(boardModel);
        return boardModel.getValidatedMoves(unvalidatedMoves);
    }

    private void showMoves(List<Move> moves) {
        System.out.println("Showing moves: ");
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
    public void handleTileClick(TileUI tileUI, TileParent tile, Move prospectiveMove) {
        System.out.println("Clicked tile: " + tileUI);
        System.out.println("Selected tile: " + selectedTile);
        if (prospectiveMove != null) { //// do move
            System.out.println("Prospective move: " + prospectiveMove);
            System.out.println("Do move");
            doMove(selectedTile, tileUI);
            // reset board UI state.
            deselect();
            return;
        }

        if (selectedTile.equals(DEV_TILE)) {
            if (tile.hasPieceMatchingTeam(boardModel.getCurrentTeam())) { //// initiate move.
                System.out.println("Initiate move");
                select(tileUI);
            }
        } else if (selectedTile.equals(tileUI)) {
            System.out.println("Cancel move");
            deselect();
        } else { //// cancel move
            if (tile.hasPieceMatchingTeam(boardModel.getCurrentTeam())) { //// cancel, select new
                System.out.println("Cancel move, select new piece");
                deselect();
                select(tileUI);
            } else { //// cancel
                System.out.println("Cancel move");
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
        Node sprite = from.removeExistingPieceSprite();
        Move move = to.getProspectiveMove();
        to.removeExistingPieceSprite();

        boardModel.doMove(move);
        to.showPieceSprite(sprite);
    }

}
