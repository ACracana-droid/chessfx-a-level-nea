package com.chessfx.ui.game;

import com.chessfx.engine.ChessGameModel;
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
    ChessGameModel boardModel;
    TileUI[][] boardUiTiles;

    public BoardUIModel(ChessGameModel boardModel, TileUI[][] boardUiTiles) {
        this.boardModel = boardModel;
        DEV_TILE = new TileUI(new VoidTile());
        this.boardUiTiles = boardUiTiles;
        selectedTile = DEV_TILE;
        prospectiveMoveTiles = new ArrayList<>();
    }

    public void select(TileUI tileUI) {
        selectedTile.setDefaultState();

        tileUI.setActive();
        selectedTile = tileUI;
        System.out.println("BoardUIModel: Selected Piece");
        showMoves(tileUI.getTile().getPiece().moves(boardModel));
    }

    private void showMoves(List<Move> moves) {
        System.out.println("Showing moves: ");
        prospectiveMoveTiles.clear();
        for (Move move : moves) {
            TileUI tileUI = boardUiTiles[move.getEndY()][move.getEndX()];
            prospectiveMoveTiles.add(tileUI);
            tileUI.setProspectiveMove(move);
            System.out.println("Tile at " + move.getEndY() + " ," + move.getEndX() + " is valid move");
        }
    }

    public void deselect() {
        selectedTile.setDefaultState();
        for (TileUI tileUI : prospectiveMoveTiles) {
            tileUI.setDefaultState();
        }
        DEV_TILE.setActive();
        selectedTile = DEV_TILE;
    }

    public void handleTileClick(TileUI tileUI, TileParent tile, Move prospectiveMove) {
        System.out.println("Clicked tile: " + tileUI);
        System.out.println("Selected tile: " + selectedTile);
        if (prospectiveMove != null) {
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
            } else { ////
                System.out.println("Cancel move");
                deselect();
            }
        }
    }

    private void doMove(TileUI from, TileUI to) {
        Node sprite = from.removeExistingPieceSprite();
        Move move = to.getProspectiveMove();
        to.removeExistingPieceSprite();

        boardModel.doMove(move);
        to.showPieceSprite(sprite);
    }

}
