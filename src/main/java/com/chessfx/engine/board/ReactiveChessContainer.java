package com.chessfx.engine.board;

import com.chessfx.engine.moves.Move;
import com.chessfx.ui.game.MoveTableData;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;
import com.chessfx.ui.game.board.BoardUIManager;
import javafx.beans.property.*;

import java.util.List;

/**
 * Related both to the UI elements and the backend board representation.
 */
public class ReactiveChessContainer extends GameBoardContainer {

    private final StringProperty currentTeamProperty = new SimpleStringProperty();
    private final IntegerProperty fullmoveCounterProperty = new SimpleIntegerProperty();
    private final StringProperty previousMoveProperty = new SimpleStringProperty("");
    private final StringProperty boardStateProperty = new SimpleStringProperty("");

    Runnable removeListeners = null;
    private BoardUIManager boardUIManager;

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
    protected void updateGame(Move move, Piece origPiece, Piece directCapture, List<Piece> indirectCaptures) {
        Log.info("Updating game...");
        super.updateGame(move, origPiece, directCapture, indirectCaptures);
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
    public void undoUpdatePiecePosition(Move move, PlyData ply) {
        // TODO: DO THIS (the super) AS TASK?
        super.undoUpdatePiecePosition(move, ply);

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

    public void setUIModel(BoardUIManager boardUIManager) {
        this.boardUIManager = boardUIManager;
    }
}
