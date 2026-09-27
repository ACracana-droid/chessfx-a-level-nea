package com.chessfx.engine.tiles;

import com.chessfx.engine.pieces.King;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.board.Team;
import com.chessfx.ui.game.tiles.TileUI;

abstract public class TileBase {

    abstract public boolean hasPiece();

    abstract public void style(TileUI tileUI);

    abstract public Piece getPiece();

    abstract public boolean isPossibleMove(Team team);

    abstract public boolean hasPieceMatchingTeam(Team currentTeam);

    abstract public Piece removePiece();

    public void setPiece(Piece piece) {
    }

    public boolean hasPiece(String pieceName) {
        return hasPiece() && getPiece().getName().equals(pieceName);
    }

    public boolean hasPiece(String pieceName, Team currentTeam) {
        if (hasPiece()) {
            Piece piece = getPiece();
            return piece.getName().equals(pieceName) && piece.getTeam().equals(currentTeam);
        }
        return false;
    }

    public boolean hasKing(Team currentTeam) {
        return hasPiece()
                && (getPiece() instanceof King)
                && getPiece().getTeam().equals(currentTeam);
    }
}
