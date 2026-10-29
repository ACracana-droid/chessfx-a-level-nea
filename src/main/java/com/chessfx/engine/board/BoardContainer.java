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
        if (gameModel.isGameOver()) {
            Log.info("Cannot move, game is over."); // kinda unnecessary seeing as the game always ends when there are no valid moves: moving is impossible.
            return;
        }

        Piece origPiece = getTile(move.startCoOrds()).getPiece();
        if (origPiece == null) {
            Log.debug("BoardContainer", getStringRepresentation());
            Log.error("BoardContainer", "Piece origin square is null!");
        }
        Piece directCapture = updatePiecePosition(move);
        if (isPromotion(move)) {
            if (this instanceof GameBoardContainer) {
                gameModel.setIsPromotion(true);
                // suspend game update until after promotion...
                // MUST NOT UNDO, etc during this time.
                return;
            } else {

            }
        }
        updateGame(move, origPiece, directCapture);
    }

    private void promote(Piece origPiece, Piece desiredPiece) {
        TileBase tile = getTile(origPiece.getCoOrds());
        tile.removePiece();
        getCurrentTeam().getPieceList().remove(origPiece);

        tile.setPiece(desiredPiece);
    }

    private boolean isPromotion(Move move) {
        if (move.destCoOrds().y() == HEIGHT - 1 || move.destCoOrds().y() == 0) {
            return true;
        }
        return false;
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
        Log.debug("BoardContainer", "Piece is originally on " + posString(move.startCoOrds()));
        piece.setCoOrds(move.destCoOrds());
        // piece's 'hasMoved' is set in updateGame.
        TileBase dest = getTile(move.destCoOrds());
        Log.debug("Dest tile piece: " + (dest.hasPiece() ? dest.getPiece().getName() : "no piece"));

        Log.debug("Current team: " + getCurrentTeam().getName() + "");
        if (move.isSuperCapture()) {
            for (Piece enemy : move.getCaptures()) {
                getTile(enemy.getCoOrds()).removePiece();
                captureListUpdate(enemy);
            }
        }

        Piece directCapture = null;

        Log.debug("Destination tile is " + posString(move.destCoOrds()));
        if (dest.hasPieceMatchingTeam(getEnemyTeam()) || dest.reallyHasPieceOpposingTeam(piece.getTeam())) {
            Log.debug("Move is capture..." + dest.getPiece().getName() + " " + dest.getPiece().getTeam().getName());
            // is capture
            directCapture = dest.getPiece();
            captureListUpdate(directCapture);
            dest.removePiece();
        }

        dest.setPiece(piece);
        return directCapture;
    }

    private TileBase getTile(String str) {
        return getTile(pos(str));
    }

    protected void captureListUpdate(Piece enemy) {
//        Log.info("Current team: " + getCurrentTeam().getName() + ", Enemy team: " + getEnemyTeam().getName());
//        Log.debug("Removing " + enemy.getTeam().getName() + " " + enemy.getName() + "" + enemy);

        getCurrentTeam().getCapturedPieces().add(enemy);
        if (!getEnemyTeam().getPieceList().remove(enemy)) {
            Log.error("BoardContainer", "Did not remove enemy piece from " + getEnemyTeam().getName());
        }
//        List<Piece> enemyList = getEnemyTeam().getPieceList();
//        for (Piece piece : enemyList) {
//            if (piece.getName().equals("queen")) {
//                Log.error("Queen", piece.getName() + " " + posString(piece.getCoOrds()) + " " + piece);
//            }
//        }
//        Log.debug("Contains queen???", "" + containsQueen(enemy.getTeam()));
    }

    private boolean containsQueen(Team enemyTeam) {
        boolean flag = false;
        for (Piece p : enemyTeam.getPieceList()) {
            if (p.getName().equals("queen")) {
                Log.error("queen", posString(p.getCoOrds()) + " " + p);
                flag = true;
            }
        }
        return flag;
    }

    protected void undoCaptureListUpdate(Piece enemy) {
        getCurrentTeam().getCapturedPieces().remove(enemy);
        getEnemyTeam().getPieceList().add(enemy);
    }

    public GameStateModel getGameModel() {
        return gameModel;
    }

    public Team getEnemyTeam() {
        return gameModel.getEnemyTeam();
    }

    public Team getCurrentTeam() {
        return gameModel.getCurrentTeam();
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
        if (plyStack.isEmpty()) {
            String msg = "Cannot undo move. No move to undo as Ply stack is empty.";
            Log.warn(msg);
            gameModel.setUndoMoveFailedReason(msg);
            return null;
        }
        if (getGameModel().isPromotion()) {
            String msg = "Cannot undo move now. Promote piece first.";
            Log.warn(msg);
            gameModel.setUndoMoveFailedReason(msg);
            return null;
        }
        PlyData ply = plyStack.pop();
        Move move = ply.move();

        undoUpdateGame();
        undoUpdatePiecePosition(move, ply.hasPieceMovedBefore(), ply.directCapture());
        return ply;
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
        } else Log.warn("Direct Capture is null...");

        getTile(move.startCoOrds()).setPiece(piece);
    }

    protected void updateGame(Move move, Piece origPiece, Piece directCapture) {
        plyStack.push(new PlyData(move, origPiece.hasMoved(), directCapture, getCurrentTeam()));
        origPiece.setHasMoved(true);
        switchTeam(true);
        Log.debug("Current Team: -> " + getCurrentTeam().getName());
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
            sb.append((i - HEIGHT) * -1).append(" | "); // length 4
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

    /**
     * Generates long algebraic notation that is valid on the standard 8x8 board and other more
     * established variants such as Capablanca Chess.
     *
     * @param move the move which was completed
     * @return algebraic notation string, e.g., e2e4
     */
    public String longAlgebraicNotation(Move move) {
        Piece piece = move.getPiece();
        //TODO: Add check (+), gameModelpromotion (e8=Q) and checkmate (#).
        //TODO: DOCUMENT
        StringBuilder sb = new StringBuilder();
        if (!piece.getFenID().equals("P"))
            sb.append(piece.getFenID());

        sb.append(getXAxisLetter(move.getStartX())).append(getYAxisNum(move.getStartY()));
        if (move.isCapture()) {
            sb.append("x");
        } else sb.append("-");

        sb.append(getXAxisLetter(move.getEndX())).append(getYAxisNum(move.getEndY()));


        return sb.toString();
    }

    public BoardState getBoardState() {
        return gameModel.getBoardState();
    }

    protected void setBoardState(BoardState boardState) {
        Log.info("BoardState is " + boardState.getText());
        gameModel.setBoardState(boardState);
    }
}
