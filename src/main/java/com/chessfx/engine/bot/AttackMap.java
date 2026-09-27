package com.chessfx.engine.bot;

import com.chessfx.engine.board.BoardContainer;
import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.King;
import com.chessfx.engine.pieces.Piece;

import java.util.List;

/**
 * Internally based on a 2D array of an equal size to the chessboard,
 * where each array position contains a list of Moves that capture the
 * attacked square.
 * <p>
 * If the list is empty, the square is safe.
 * </p>
 */
public class AttackMap {

    private final MoveListWrapper[][] board;
    private final int HEIGHT;
    private final int WIDTH;

    public AttackMap(BoardContainer boardContainer) {
        HEIGHT = boardContainer.HEIGHT;
        WIDTH = boardContainer.WIDTH;
        board = initialiseMap();
    }

    private MoveListWrapper[][] initialiseMap() {
        MoveListWrapper[][] board = new MoveListWrapper[HEIGHT][WIDTH];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j] = new MoveListWrapper();
            }
        }
        return board;
    }

    public void clear() {
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                board[i][j].clear();
            }
        }

    }

    public void addAttacks() {

    }

    // TODO: FINISH, and split for multiple reams.
    public void calculate(BoardContainer boardContainer) {
        calculate(boardContainer, boardContainer.getCurrentTeam());
    }

    public void calculate(BoardContainer boardContainer, Team team) {
        clear();
        for (Piece piece : team.getPieceList()) {
            for (Move move : piece.captures(boardContainer)) {
                putOnMap(move);
            }
        }
    }

    public List<Move> getMoves(int y, int x) {
        return board[y][x].getList();
    }

    private void putOnMap(Move move) {
        for (Piece p : move.getCaptures())
            board[p.getCoOrds()[0]][p.getCoOrds()[1]].add(move);
    }

    public MoveListWrapper[][] board() {
        return board;
    }

    public List<Move> getMoves(int[] coOrds) {
        return board[coOrds[0]][coOrds[1]].getList();
    }

    public boolean isTileThreatened(int[] coOrds) {
        return !board[coOrds[0]][coOrds[1]].isEmpty();
    }

    public boolean isTileThreatened(King king) {
        return isTileThreatened(king.getCoOrds());
    }
}
