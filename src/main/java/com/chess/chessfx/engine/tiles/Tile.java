package com.chess.chessfx.engine.tiles;

import com.chess.chessfx.engine.pieces.Piece;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.gameui.tiles.TileUI;

public class Tile extends TileParent {
    Piece piece;

    public Tile(Piece piece) {
        super();
        if (piece != null)
            this.piece = piece;
    }

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


}
