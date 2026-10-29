package com.chessfx.engine.board;

import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
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

    protected String getYAxisNum(int y) {
        int num = (y - HEIGHT) * -1;
        return Integer.toString(num);
    }

    protected char getXAxisLetter(int x) {
        return (char) (x + 'a');
    }

    public Coordinate pos(String str) {
        Log.debug("", posString(new Coordinate(getInternalY(str.charAt(1)), getInternalX(str.charAt(0)))));
        return new Coordinate(getInternalY(str.charAt(1)), getInternalX(str.charAt(0)));
    }

    public String posString(Coordinate coordinate) {
        return getXAxisLetter(coordinate.x()) + getYAxisNum(coordinate.y());
    }

    private int getInternalX(char c) {
        int num = (int) c - 'a';
        return num;
    }

    private int getInternalY(char c) {
        int num = (HEIGHT - Character.getNumericValue(c));
        return num;
    }



}
