package com.chess.chessfx.engine.tiles;

import com.chess.chessfx.engine.pieces.Piece;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.gameui.tiles.TileUI;

abstract public class TileParent {

    abstract public boolean hasPiece();

    abstract public void style(TileUI tileUI);

    abstract public Piece getPiece();

    abstract public boolean isPossibleMove(Team team);
}
