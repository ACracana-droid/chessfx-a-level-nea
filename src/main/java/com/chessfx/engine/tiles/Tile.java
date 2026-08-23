package com.chessfx.engine.tiles;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.pieces.Team;
import com.chessfx.ui.game.tiles.TileUI;

public class Tile extends TileParent {
    Piece piece;

    public Tile(Piece piece) {
        super();
        if (piece != null)
            this.piece = piece;
    }

    @Override
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public boolean hasPiece() {
        return piece != null;
    }

    @Override
    public void style(TileUI tileUI) {
        tileUI.getStyleClass().add("chess-tile");
    }

    @Override
    public Piece getPiece() {
        return piece;
    }

    @Override
    public boolean isPossibleMove(Team team) {
        return !hasPiece() || (piece.getTeam().equals(team));
    }

    @Override
    public boolean hasPieceMatchingTeam(Team currentTeam) {
        return hasPiece() && piece.getTeam().equals(currentTeam);
    }

    @Override
    public Piece removePiece() {
        Piece removed = piece;
        this.piece = null;
        return removed;
    }


}
