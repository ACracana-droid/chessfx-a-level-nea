package com.chessfx.engine.bot;

import com.chessfx.engine.board.*;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.log.AnsiColourCode;
import com.chessfx.log.Log;

import java.util.List;

/**
 * Internally based on a 2D array of an equal size to the chessboard,
 * where each array position contains a list of Moves that capture the
 * attacked square.
 * <p>
 * If the list is empty, the square is safe.
 * </p>
 */
public class AttackMap implements Cloneable {

    private final int HEIGHT;
    private final int WIDTH;
    private final boolean[][] boolBoard;

    protected AttackMap(int height, int width, boolean[][] boolBoard) {
        HEIGHT = height;
        WIDTH = width;
        this.boolBoard = copyBoard(boolBoard);
    }

    private boolean[][] copyBoard(boolean[][] board) {
        if (board.length < 1) return new boolean[][]{};
        int n = board.length;
        int m = board[0].length;
        boolean[][] copy = new boolean[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }

    public String logOutputStringRepresentation() {
        Log sb = Log.LOG; //

        sb.append("\n");
        sb.append("_".repeat((3 * WIDTH) + 4 + 2));
        sb.append("\n");
        // must traverse board from 8, downwards.
        boolean checkerPattern = true;
        //// using i to make rank index
        for (int i = HEIGHT; i > 0; ) {
            sb.append("" + i).append(" | "); // length 4
            i--;
            for (boolean tile : boolBoard[i]) {
                sb.append(" ");
                sb.append(tile ? AnsiColourCode.GREEN + "T" + AnsiColourCode.RESET : AnsiColourCode.RED + "F" + AnsiColourCode.RESET);
                sb.append(" ");

            }
            sb.append(" |\n"); //length 2*
            checkerPattern = !checkerPattern;
        }
        sb.append("_".repeat((3 * WIDTH) + 4 + 2));
        sb.append("\n").
                append(" ".repeat(4 + 1));
        //// making file index
        for (char i = 'a'; i <= 'h'; i++) {
            sb.append(" " + i).append(" ".repeat(2));
        }
        return sb.toString();
    }

    public enum AttackStatus {
        ATTACK,
        NO_ATTACK
    }


    public AttackMap(BoardContainerBase boardContainer) {
        HEIGHT = boardContainer.HEIGHT;
        WIDTH = boardContainer.WIDTH;
        boolBoard = new boolean[HEIGHT][WIDTH];
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
                boolBoard[i][j] = false;
            }
        }

    }


    // TODO: FINISH, and split for multiple reams.
    public void calculate(BoardContainer boardContainer) {
        calculateBools(boardContainer, boardContainer.getEnemyTeam());
    }

    public void calculate(BoardContainerBase boardContainer, Team team) {
        calculateBools(boardContainer, team);
    }

    private void calculateBools(BoardContainerBase boardContainer, Team team) {
        clear();
        Log.debug("Calculating bools...");
        for (Piece piece : team.getPieceList()) {
            for (Move move : piece.threats(boardContainer)) {
                addCapturesToBoolBoard(move);
            }
        }
        Log.debug("Finished loop.");
    }

    private void addCapturesToBoolBoard(Move move) {
        if (move.isSuperCapture()) {
            List<Piece> captures = move.getCaptures();
            for (Piece capture : captures) {
                Coordinate coOrds = capture.getCoOrds();
                boolBoard[coOrds.y()][coOrds.x()] = true;
            }
        }
        boolBoard[move.getEndY()][move.getEndX()] = true;
    }


    public boolean getTile(Coordinate coOrds) {
        return boolBoard[coOrds.y()][coOrds.x()];
    }

    public boolean getTile(int i, int j) {
        return boolBoard[i][j];
    }

    @Override
    public AttackMap clone() {
        AttackMap clone = new AttackMap(HEIGHT, WIDTH, boolBoard);
        return clone;
    }
}
