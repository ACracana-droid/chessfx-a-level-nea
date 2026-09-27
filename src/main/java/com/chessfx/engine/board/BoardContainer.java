package com.chessfx.engine.board;

import com.chessfx.engine.bot.AttackMap;
import com.chessfx.engine.bot.Bot;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;

import java.util.ArrayDeque;
import java.util.List;


public class BoardContainer extends BoardContainerBase {

    protected final GameStateModel gameModel;
    protected Bot bot;

    protected ArrayDeque<PlyData> plyStack = new ArrayDeque<>();

    AttackMap attackMap;

    public BoardContainer(GameStateModel gameModel, TileBase[][] board) {
        super(board);
        this.gameModel = gameModel;
        attackMap = new AttackMap(this);
    }

    public boolean tileHasPieceMatchingTeam(int y, int x) {
        return board[y][x].hasPieceMatchingTeam(gameModel.getCurrentTeam());
    }

    public void terminate() {
        // do nothing. Equivalent to an abstract method
    }

    public Team getCurrentTeam() {
        return gameModel.getCurrentTeam();
    }

    /**
     *
     * <p>
     * Completes a move in its entirety. It does this by updating the position of the moved piece to the end
     * location of the move and by removing any pieces 'captured' by this move from the board.
     * <p/>
     * <p>
     * Also updates the game state and pushes the completed move to the moveStack so that it can be undone.
     * </p>
     * <p>
     * If the current team after the game update is a bot, then this method also calls for the bot to take
     * its turn.
     * </p>
     *
     * @param move the move being done
     */
    public void doMove(Move move) {
        updatePiecePosition(move);
        updateGame(move);
        if (getCurrentTeam().isBot()) {
            takeTurnAsBot();
        }
    }


    /**
     * Internally completes the move on the TileParent array board.
     * <p>
     * It removes the piece from the TileParent addressed by the initial coordinates of the move,
     * and places that piece onto the TileParent addressed by the destination coordinates of the move.
     * <p/>
     *
     * @param move the move being made
     */
    protected void updatePiecePosition(Move move) {
        Piece piece = getTile(move.getStartY(), move.getStartX()).removePiece();
        piece.setCoOrds(move.getEndY(), move.getEndX());
        // piece's 'hasMoved' is set in updateGame.

        if (move.isCapture()) {
            // for things like en passant
            getCurrentTeam().getCapturedPieces().addAll(move.getCaptures());
            // handle capture
        }
        getTile(move).setPiece(piece);
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
    public Move undoLastMove() {
        if (plyStack.isEmpty()) return null;
        PlyData ply = plyStack.pop();
        Move move = ply.getMove();

        undoUpdateGame();
        undoUpdatePiecePosition(move, ply.hasPieceMovedBefore());
        return move;
    }

    private void undoUpdateGame() {
        switchTeam(false);
    }

    protected void updateGame(Move move) {
        Piece piece = move.getPiece();
        plyStack.push(new PlyData(move, piece.hasMoved(), getCurrentTeam()));
        piece.setHasMoved(true);
        switchTeam(true);
        // MUST CHECK REPERCUSSIONS OF MOVE.
        // TODO: REPLACE WITH SUPERIOR VER BELOW
//        if (Bot.isCheck(this)) {
//            setBoardState(BoardState.CHECK);
//        }
        attackMap.calculate(this);
        setBoardState(queryBoardState());

//        Log.info("Board Container", "King is threatened? " + bot.isKingThreatenedThorough(this));

    }

    private BoardState queryBoardState() {
        if (isCheck()) {
            if (isCheckmate()) {
                return BoardState.CHECKMATE;
            }
            return BoardState.CHECK;
        }

        return BoardState.DEFAULT;
    }

    public boolean isCheck() {
        return !attackMap.isTileThreatened(getCurrentTeam().getKing());
    }

    public boolean isCheckmate() {
        return false;
    }


    protected void setBoardState(BoardState boardState) {
        Log.info("BoardState is " + boardState.getText());
        gameModel.setBoardState(boardState);
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

    protected void undoUpdatePiecePosition(Move move, boolean hasMoved) {
        Log.info("Undoing move...");
        Piece piece = getTile(move).removePiece();
        piece.setCoOrds(move.getStartY(), move.getStartX());
        piece.setHasMoved(hasMoved);

        if (move.isCapture()) {
            // for things like en passant
            getCurrentTeam().getCapturedPieces().removeAll(move.getCaptures());
            // handle capture removeCapture
            for (Piece p : move.getCaptures()) {
                getTile(p.getCoOrds()).setPiece(p);
            }
        }
        getTile(move.getStartY(), move.getStartX()).setPiece(piece);
    }

    public GameStateModel getGameModel() {
        return gameModel;
    }

    public List<Move> getValidatedMoves(List<Move> unvalidatedMoves) {
        // validate moves by checking if causes team side check.
        // gotta move hypothetically.
        // need hypothetical board.


        return unvalidatedMoves;
    }

}
