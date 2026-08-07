package com.chess.chessfx.engine.tiles;

import com.chess.chessfx.engine.pieces.Piece;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.gameui.tiles.TileUI;

/**
 * This is the ancestor of tiles which other pieces cannot go on.
 * These tiles have no field to hold pieces and will return 'false'
 */
public class BarrierTile extends TileParent {

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


}
