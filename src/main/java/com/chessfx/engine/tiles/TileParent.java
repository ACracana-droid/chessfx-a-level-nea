package com.chessfx.engine.tiles;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.pieces.Team;
import com.chessfx.ui.game.tiles.TileUI;

abstract public class TileParent {

    abstract public boolean hasPiece();

    abstract public void style(TileUI tileUI);

    abstract public Piece getPiece();

    abstract public boolean isPossibleMove(Team team);

    abstract public boolean hasPieceMatchingTeam(Team currentTeam);

    abstract public Piece removePiece();

    public void setPiece(Piece piece) {
    }
}
