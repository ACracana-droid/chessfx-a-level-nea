package com.chessfx.engine.board;

import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;

public class BoardContainerBase implements GridContainer<TileBase> {

    protected final TileBase[][] board;
    public final int HEIGHT;
    public final int WIDTH;


    public BoardContainerBase(TileBase[][] board) {
        this.board = board;
        HEIGHT = board.length;
        WIDTH = board[0].length;
    }


    public boolean inBoardBounds(int y, int x) {
        return 0 <= y && y < HEIGHT
                && 0 <= x && x < WIDTH;
    }

    public boolean tileHasPiece(int rank, int file) {
        return board[rank][file].hasPiece();
    }

    public boolean tileHasPieceMatchingTeam(int y, int x, Team team) {
        return board[y][x].hasPieceMatchingTeam(team);
    }

    // shouldn't have to pass the team, it must be currentTeam
    public boolean isPossibleMove(Team team, int rank, int file) {
        return !board[rank][file].hasPieceMatchingTeam(team);
    }


    public TileBase getTile(int y, int x) {
        return board[y][x];
    }

    @Override
    public TileBase getTile(Coordinate coOrds) {
        return board[coOrds.y()][coOrds.x()];
    }

    /**
     * @param move the move which ends at the desired tile.
     * @return the TileBase at the end co-ordinates of the move.
     */
    public TileBase getTile(Move move) {
        return getTile(move.destCoOrds());
    }


    @Override
    public TileBase[][] getBoard() {
        return board;
    }



}
