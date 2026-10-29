package com.chessfx.engine.tiles;

import com.chessfx.engine.pieces.ArbitraryPieceFactory;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.board.Team;
import com.chessfx.log.Log;
import com.chessfx.ui.game.tiles.TileUI;

public class Tile extends TileBase {

    private Piece piece;

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
    public boolean hasPieceMatchingTeam(Team team) {
        return hasPiece() && piece.getTeam().equals(team);
    }

    @Override
    public Piece removePiece() {
        Piece removed = piece;
        this.piece = null;
        return removed;
    }

    @Override
    public TileBase copy(Team team, ArbitraryPieceFactory pieceFactory) {
        Piece pieceCopy = pieceFactory.copy(piece, team);
        Tile tile = new Tile(pieceCopy);
        return tile;

    }

    @Override
    public TileBase copy() {
        return new Tile(null);
    }
}
