package com.chessfx.engine;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileParent;
import javafx.beans.property.*;

public class ReactiveChessContainer extends BoardContainer {

    private final StringProperty currentTeamProperty = new SimpleStringProperty();
    public final IntegerProperty fullmoveCounterProperty = new SimpleIntegerProperty();
    private final StringProperty previousMoveProperty = new SimpleStringProperty();


    Runnable removeListeners = null;

    public ReactiveChessContainer(GameStateModel gameModel, TileParent[][] board) {
        super(gameModel, board);
        fullmoveCounterProperty.set(gameModel.getFullmoveCounter());
    }

    public IntegerProperty fullmoveCounterProperty() {
        return fullmoveCounterProperty;
    }

    public boolean tileHasPieceMatchingTeam(int y, int x) {
        return super.tileHasPieceMatchingTeam(y, x);
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

        Piece piece = board[move.y1][move.x1].getPiece();

        // Must set move before 'doing move'
        previousMoveProperty.set(longAlgebraicNotation(piece, move));
        // doMove calls update game, which causes team to switch.
        // This is the logical order.
        super.doMove(move);
    }

    @Override
    protected void updateGame(Move move) {
        System.out.println("UPDATING GAME: ");
        moveStack.push(move);
        switchTeams(gameModel);
//        setBoardState(gameModel, board);

    }

    private void setBoardState(GameStateModel gameModel, TileParent[][] board) {
        BoardState state = ChessEngine.getBoardState(gameModel, board);
        gameModel.setBoardState(state);

    }


    /**
     * Generates long algebraic notation that is valid on the standard 8x8 board and other more
     * established variants such as Capablanca Chess.
     *
     * @param piece the piece which did the move
     * @param move  the move which was completed
     * @return algebraic notation string, e.g., e2e4
     */
    public String longAlgebraicNotation(Piece piece, Move move) {
        //TODO: Add check (+), promotion (e8=Q) and checkmate (#).
        //TODO: DOCUMENT
        StringBuffer sb = new StringBuffer();
        if (!piece.getFenID().equals("P"))
            sb.append(piece.getFenID());

        sb.append(getXAxisLetter(move.x1)).append(getYAxisNum(move.y1));
        if (move.isCapture())
            sb.append("x");
        else
            sb.append("-");
        sb.append(getXAxisLetter(move.x2)).append(getYAxisNum(move.y2));
        return sb.toString();
    }

    private String getYAxisNum(int y) {
        int num = (y - gameModel.getHeight()) * -1;
        return Integer.toString(num);
    }

    private char getXAxisLetter(int x) {
        return (char) (x + 'a');
    }

    private void switchTeams(GameStateModel gameModel) {
        if (gameModel.cycleTeam()) {
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
}
