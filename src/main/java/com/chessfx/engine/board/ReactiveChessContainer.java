package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.moves.MoveTableData;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;
import javafx.beans.property.*;

public class ReactiveChessContainer extends GameBoardContainer {

    private final StringProperty currentTeamProperty = new SimpleStringProperty();
    private final IntegerProperty fullmoveCounterProperty = new SimpleIntegerProperty();
    private final StringProperty previousMoveProperty = new SimpleStringProperty("");
    private final StringProperty boardStateProperty = new SimpleStringProperty("");

    Runnable removeListeners = null;

    public ReactiveChessContainer(GameStateModel gameModel,
                                  TileBase[][] board) {
        super(gameModel, board);
        Log.debug("Constructing ReactiveChessContainer");
        // Set properties to initial values. This is before reactive elements are established
        fullmoveCounterProperty.set(gameModel.getFullmoveCounter());
        currentTeamProperty.set(gameModel.getCurrentTeam().getName());
        boardStateProperty.set(gameModel.getBoardState().getText());
    }

    public IntegerProperty fullmoveCounterProperty() {
        return fullmoveCounterProperty;
    }


    /**
     * Updates the properties for the UI and calls the superclass method
     * to internally complete the move and trigger the game update.
     *
     * @param move the move being done
     */
    @Override
    public void doMove(Move move) {
        // Order matters

        // Must set move before 'doing move'
        Log.info("ReactiveChessContainer", "Updating the moveProperty (The MoveTable should change).");
        updateMoveProperty(false, longAlgebraicNotation(move));
        // doMove calls update game, which causes team to switch.
        // This is the logical order.
        // Do as task.

        super.doMove(move);
    }

    @Override
    protected void updateGame(Move move, Piece origPiece, Piece directCapture) {
        Log.info("Updating game...");
        super.updateGame(move, origPiece, directCapture);
        // other reactive stuff...
        debugQuery();
//        setBoardState(gameModel, board);
    }

    private void debugQuery() {
        boolean b = board[3][0].hasPiece();
        Log.warn("Piece at 3,0? -> " + b);
    }

    @Override
    protected void setBoardState(BoardState boardState) {
        super.setBoardState(boardState);
        boardStateProperty.set(boardState.getText());
    }


    @Override
    public void undoUpdatePiecePosition(Move move, boolean hasMoved, Piece piece) {
        // TODO: DO THIS (the super) AS TASK?
        super.undoUpdatePiecePosition(move, hasMoved, piece);

        if (plyStack.isEmpty()) {
            updateMoveProperty(true, "");
            return;
        }
        PlyData previous = plyStack.peek();
        updateMoveProperty(true, longAlgebraicNotation(previous.move()));

    }

    private void updateMoveProperty(boolean isUndo, String moveString) {
        Log.info("moveProperty was:" + previousMoveProperty.get() + "\nmoveProperty is now:" + moveString);
        gameModel.setIsMoveUndo(isUndo);
        previousMoveProperty.set(moveString);
    }
//
//    private void setBoardState(GameStateModel gameModel, TileBase[][] board) {
//        BoardState state = ChessEngine.getBoardState(gameModel, board);
//        gameModel.setBoardState(state);
//
//    }


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

    private String getYAxisNum(int y) {
        int num = (y - HEIGHT) * -1;
        return Integer.toString(num);
    }

    private char getXAxisLetter(int x) {
        return (char) (x + 'a');
    }

    @Override
    protected void switchTeam(boolean isForwards) {
        System.out.println("Reactively Switching Teams...");
        int diff = gameModel.cycleTeam(isForwards).getValue();
        if (diff != 0) {
            fullmoveCounterProperty.set(gameModel.getFullmoveCounter());
        }
        currentTeamProperty.set(gameModel.getCurrentTeam().getName());
    }


    public StringProperty currentTeamStringProperty() {
        return currentTeamProperty;
    }


    public MoveTableData getMoveTableData() {
        MoveTableData moveTableData = new MoveTableData(
                gameModel, fullmoveCounterProperty, previousMoveProperty
        );
        removeListeners = moveTableData.getRemoveListenersRunnable();
        return moveTableData;
    }

    @Override
    public void terminate() {
        removeListeners.run();
    }


    public final StringProperty boardStateProperty() {
        return boardStateProperty;
    }
}
