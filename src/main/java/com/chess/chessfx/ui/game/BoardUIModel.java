package com.chess.chessfx.ui.game;

import com.chess.chessfx.engine.ChessGameModel;
import com.chess.chessfx.engine.Move;
import com.chess.chessfx.engine.MoveHandler;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.engine.tiles.TileParent;
import com.chess.chessfx.engine.tiles.VoidTile;
import com.chess.chessfx.ui.game.tiles.TileUI;

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
            tileUI.setMoveHighlight(move.isCapture());
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

    public void handleTileClick(TileUI tileUI, TileParent tile) {
        System.out.println("Clicked tile: " + tileUI);
        System.out.println("Selected tile: " + selectedTile);
        if (selectedTile.equals(DEV_TILE)) {
            if (tile.hasPieceMatchingTeam(boardModel.getCurrentTeam())) { //// initiate move.
                System.out.println("Initiate move");
                select(tileUI);
            }
        } else if (selectedTile.equals(tileUI)) {
            deselect();
        } else { //// either move or cancel move
            if (tile.hasPieceMatchingTeam(boardModel.getCurrentTeam())) { //// cancel, select new
                System.out.println("Cancel move, select new piece");
                deselect();
                select(tileUI);
            } else { //// move

                if (tileUI.isMoveHighlighted()) {
                    System.out.println("Do move");
                    deselect();
                    // do move
                } else {
                    System.out.println("Cancel move");
                    deselect();
                }
            }
        }
    }


}
