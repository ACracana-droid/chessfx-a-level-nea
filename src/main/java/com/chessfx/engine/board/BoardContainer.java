package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;

import java.util.ArrayDeque;

public class BoardContainer extends BoardContainerBase {


    // ENSURE TEAMS ARE READ ONLY (so it does not affect the child class).
    protected final GameStateModel gameModel;
    protected ArrayDeque<PlyData> plyStack = new ArrayDeque<>();


    public BoardContainer(GameStateModel gameModel, TileBase[][] board) {
        super(board);
        this.gameModel = gameModel;
    }

    public void doMove(Move move) {
        Piece origPiece = getTile(move.startCoOrds()).getPiece();
        Piece directCapture = updatePiecePosition(move);
        updateGame(move, origPiece, directCapture);
    }

    /**
     * Internally completes the move on the TileParent array board.
     * <p>
     * It removes the piece from the TileParent addressed by the initial coordinates of the move,
     * and places that piece onto the TileParent addressed by the destination coordinates of the move.
     * <p/>
     *
     * @param move the move being made
     * @return the enemy piece at the destination of the move
     */
    protected Piece updatePiecePosition(Move move) {
        Piece piece = getTile(move.startCoOrds()).removePiece();
        Log.warn(move.getStartY() + ", " + move.getStartX());
        piece.setCoOrds(move.destCoOrds());
        // piece's 'hasMoved' is set in updateGame.
        TileBase dest = getTile(move.destCoOrds());


        if (move.isSuperCapture()) {
            for (Piece enemy : move.getCaptures()) {
                getTile(enemy.getCoOrds()).removePiece();
                captureListUpdate(enemy);
            }
        }

        Piece directCapture = null;

        if (dest.hasPieceMatchingTeam(getEnemyTeam())) {
            // is capture
            directCapture = dest.getPiece();
            captureListUpdate(directCapture);
            dest.removePiece();
        }

        dest.setPiece(piece);
        return directCapture;
    }

    protected void captureListUpdate(Piece enemy) {
    }

    protected void undoCaptureListUpdate(Piece enemy) {
    }

    public GameStateModel getGameModel() {
        return gameModel;
    }

    public Team getEnemyTeam() {
        return gameModel.getEnemyTeam();
    }

    /**
     * <p>
     * Internally undoes the move that was last completed. This includes changing
     * the piece position and popping the ply data off the plyStack.
     * </p>
     * <p>
     * This is the exact reverse of 'doMove(Move)'.
     * </p>
     *
     * @return move the move being undone
     */
    public PlyData undoLastMove() {
        if (plyStack.isEmpty()) return null;
        PlyData ply = plyStack.pop();
        Move move = ply.move();

        undoUpdateGame();
        undoUpdatePiecePosition(move, ply.hasPieceMovedBefore(), ply.directCapture());
        return ply;
    }



    public Team getCurrentTeam() {
        return gameModel.getCurrentTeam();
    }

    protected void takeTurnAsBot() {
        // TODO: TAKE TURN AS BOT.
        Log.info("Taking turn as bot...");
        Log.warn("UNIMPLEMENTED");
        // Need recursive algorithm and board evaluation.
    }

    protected void switchTeam(boolean isForwards) {
        gameModel.cycleTeam(isForwards);
    }

    protected void undoUpdatePiecePosition(Move move, boolean hasMoved, Piece directCapture) {
        Log.info("Undoing move...");
        Piece piece = getTile(move).removePiece();
        piece.setCoOrds(move.startCoOrds());
        piece.setHasMoved(hasMoved);

        if (move.isSuperCapture()) {
            // for things like en passant
            // handle capture removeCapture
            for (Piece enemy : move.getCaptures()) {
                undoCaptureListUpdate(enemy);
                getTile(enemy.getCoOrds()).setPiece(enemy);
            }
        }
        if (directCapture != null) {
            undoCaptureListUpdate(directCapture);
            getTile(move.destCoOrds()).setPiece(directCapture);
        }

        getTile(move.startCoOrds()).setPiece(piece);
    }

    protected void updateGame(Move move, Piece origPiece, Piece directCapture) {
        plyStack.push(new PlyData(move, origPiece.hasMoved(), directCapture, getCurrentTeam()));
        origPiece.setHasMoved(true);
        switchTeam(true);
    }

    protected void undoUpdateGame() {
        switchTeam(false);
    }

    public String getStringRepresentation() {
        StringBuilder sb = new StringBuilder(); //

        sb.append("_".repeat((3 * WIDTH) + 4 + 2));
        sb.append("\n");
        // must traverse board from 8, downwards.
        boolean checkerPattern = true;
        //// using i to make rank index
        for (int i = HEIGHT; i > 0; ) {
            sb.append(i).append(" | "); // length 4
            i--;
            for (TileBase tile : board[i]) {
                sb.append(" ");
                if (tile.hasPiece()) {
                    sb.append(tile.getPiece().getFenID());
                } else {
                    sb.append(checkerPattern ? '#' : '-'); //// white symbol, black symbol
                }
                checkerPattern = !checkerPattern;

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
            sb.append(i).repeat(" ", 2);
        }
        return sb.reverse().toString();
    }

}
