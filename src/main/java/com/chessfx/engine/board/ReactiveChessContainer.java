package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.engine.moves.MoveTableData;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;
import com.chessfx.ui.game.board.BoardUIModel;
import javafx.beans.property.*;

public class ReactiveChessContainer extends GameBoardContainer {

    private final StringProperty currentTeamProperty = new SimpleStringProperty();
    private final IntegerProperty fullmoveCounterProperty = new SimpleIntegerProperty();
    private final StringProperty previousMoveProperty = new SimpleStringProperty("");
    private final StringProperty boardStateProperty = new SimpleStringProperty("");

    Runnable removeListeners = null;
    private BoardUIModel boardUIModel;

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

        if (gameModel.isPromotion()) {
            boardUIModel.promotionPrompt();
        }
    }

    @Override
    protected void updateGame(Move move, Piece origPiece, Piece directCapture) {
        Log.info("Updating game...");
        super.updateGame(move, origPiece, directCapture);
        // other reactive stuff...
//        setBoardState(gameModel, board);
    }

    @Override
    protected void setBoardState(BoardState boardState) {
        super.setBoardState(boardState);
        boardStateProperty.set(boardState.getText());
    }

    @Override
    protected void endGame() {
        super.endGame();
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

    @Override
    protected void undoUpdateGame() {
        super.undoUpdateGame();
        // TODO: consider whether to use the boardUiModel...
//        boardUIModel.showAttackMap(attackMap);
    }

    private void updateMoveProperty(boolean isUndo, String moveString) {
        Log.info("moveProperty was:" + previousMoveProperty.get() + "\nmoveProperty is now:" + moveString);
        gameModel.setIsMoveUndo(isUndo);
        previousMoveProperty.set(moveString);

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

    public void setUIModel(BoardUIModel boardUIModel) {
        this.boardUIModel = boardUIModel;
    }
}
