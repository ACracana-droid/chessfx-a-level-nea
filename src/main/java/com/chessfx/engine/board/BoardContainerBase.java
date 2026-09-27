package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.tiles.TileBase;

public class BoardContainerBase {

    protected final TileBase[][] board;
    public final int HEIGHT;
    public final int WIDTH;


    public BoardContainerBase(TileBase[][] board) {
        this.board = board;
        HEIGHT = board.length;
        WIDTH = board[0].length;
    }

    protected static TileBase[][] copyOfBoard(TileBase[][] board) {
        if (board.length < 1) return new TileBase[][]{};
        int n = board.length;
        int m = board[0].length;
        TileBase[][] copy = new TileBase[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
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

    public TileBase getTile(int[] coOrds) {
        return board[coOrds[0]][coOrds[1]];
    }

    /**
     * @param move the move which ends at the desired tile.
     * @return the TileBase at the end co-ordinates of the move.
     */
    public TileBase getTile(Move move) {
        return board[move.getEndY()][move.getEndX()];
    }


    public TileBase[][] getBoard() {
        return board;
    }

}
