package com.chessfx.engine.tiles;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.board.Team;
import com.chessfx.ui.game.tiles.TileUI;

/**
 * This is the ancestor of tiles which other pieces cannot go on.
 * These tiles have no field to hold pieces and will return 'false'
 */
public class BarrierTile extends TileBase {

    public BarrierTile() {

    }
    @Override
    public boolean hasPiece() {
        return false;
    }

    @Override
    public void style(TileUI tileUI) {
        tileUI.getStyleClass().add("barrier-tile");
    }

    @Override
    public Piece getPiece() {
        return null;
    }

    @Override
    public boolean isPossibleMove(Team team) {
        return false;
    }

    @Override
    public boolean hasPieceMatchingTeam(Team currentTeam) {
        return false;
    }

    @Override
    public Piece removePiece() {
        return null;
    }


}
