package com.chessfx.engine.board;

import com.chessfx.engine.bot.AttackMap;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;


public class GameBoardContainer extends BoardContainer {


    protected AttackMap attackMap;
    protected BoardContainer virtualBoard;


    public GameBoardContainer(GameStateModel gameModel, TileBase[][] board) {
        super(gameModel, board);

        this.virtualBoard = null;
        this.attackMap = new AttackMap(this);
    }


    @Override
    protected void updateGame(Move move, Piece origPiece, Piece directCapture) {
        super.updateGame(move, origPiece, directCapture);

//        attackMap.calculate(virtualBoard, virtualBoard.getEnemyTeam());
        setBoardState(queryBoardState());
        if (gameModel.isGameOver()) {
            endGame();
            return;
        }

        if (getCurrentTeam().isBot()) {
            takeTurnAsBot();
        }
    }

    protected void endGame() {
        Log.info("Game has ended by " + getBoardState().getText() + "!");
        if (getBoardState().equals(BoardState.CHECKMATE)) {
            Log.info(getEnemyTeam().getName() + " has won.");
        }
    }


    @Override
    protected void undoUpdateGame() {
        super.undoUpdateGame();
//        attackMap.calculate(virtualBoard);
        setBoardState(queryBoardState());
    }

    /**
     * Called after class is fully constructed.
     */
    public void gameStart() {
        // Initial game update:


        virtualBoard = makeIsolatedBoardContainer();

//        attackMap.calculate(virtualBoard);

        setBoardState(queryBoardState());
    }

    private BoardContainer makeIsolatedBoardContainer() {
        List<Team> teamList = gameModel.copyTeams();
        GameStateModel gameStateModel = new GameStateModel(gameModel, teamList);
        TileBase[][] board = boardCopy(this.board, teamList);

        BoardContainer isolatedBoardContainer = new BoardContainer(gameStateModel, board);
        return isolatedBoardContainer;
    }

    private TileBase[][] boardCopy(TileBase[][] board, List<Team> teamList) {
        int m = board.length, n = board[0].length;

        TileBase[][] copy = new TileBase[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                TileBase tile = board[i][j];
                if (tile.hasPiece()) {

                    Piece piece = tile.getPiece();
                    Team team = teamList.get(piece.getTeam().idx());

                    copy[i][j] = tile.copy(team, gameModel.getChessConfig().getPieceFactory());
                } else {
                    copy[i][j] = tile.copy();
                }
            }
        }
        return copy;
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
    @Override
    public void doMove(Move move) {
        // This was a major bug when order is swapped...
        virtualBoard.doMove(move);
        super.doMove(move);
    }

    public void doMoveWithValidation(Coordinate origin, Coordinate destination) {
        Piece piece = getTile(origin).getPiece();
        if (piece == null) {
            Log.error("BoardContainer", "Piece does not exist at origin coordinate. Move not made.");
            return;
        }

        Move desiredMove = null;

        for (Move move : getValidatedMoves(piece.moves(this))) {
            if (move.getStartX() == origin.x() && move.getStartY() == origin.y() && move.getEndX() == destination.x() && move.getEndY() == destination.y()) {
                desiredMove = move;
                break;
            }
        }
        if (desiredMove == null) {
            Log.error("BoardContainer", "Move is not a member of the list of valid moves of the piece at specified original coordinate. Move not made.");
            return;
        }
        doMove(desiredMove);
    }

    public void playMoves(List<String> moveStrings) {
        for (String moveString : moveStrings) {
            doMoveWithValidation(pos(moveString.substring(0, 2)), pos(moveString.substring(3)));
        }
    }

    @Override
    public PlyData undoLastMove() {
        PlyData ply = super.undoLastMove();
        virtualBoard.undoLastMove();
        return ply;
    }

    public List<Move> getValidatedMoves(List<Move> unvalidatedMoves) {
        List<Move> validated = new ArrayList<>();

        // TODO: The reason this does not work is because of the Team object which does not have a clone.
        // The various lists that Team maintains are in disarray. we could not update them in BoardContainer...
        // But getting accurate king positions in the virtual board will be nigh impossible.

        for (Move move : unvalidatedMoves) {
            // say team is black
            Log.debug("(pre) TEAM is" + virtualBoard.getCurrentTeam().getName());

            virtualBoard.doMove(move);
            Log.debug("VirtualBoard representation:\n", virtualBoard.getStringRepresentation());

            // now curr team is light
            attackMap.calculate(virtualBoard, virtualBoard.getCurrentTeam());
            Log.debug("(post) TEAM is" + virtualBoard.getCurrentTeam().getName());

            // thus enemy team is black again...
            Log.debug("AttackMap");
            attackMap.logOutputRepresentation();
            if (!isCheck(virtualBoard.getEnemyTeam())) {
                /// not check, therefore move is valid
                validated.add(move);
            }


            virtualBoard.undoLastMove();
//            Log.debug("VirtualBoard representation:\n", virtualBoard.getStringRepresentation());
        }


        return validated;
    }

    private boolean isCheck(Team team) {
        return attackMap.getTile(team.getKing().getCoOrds());
    }

    public void terminate() {
        // do nothing. Equivalent to an abstract method
    }


    public AttackMap getAttackMap() {
        return attackMap;
    }

    // TODO: document pseudocode
    protected BoardState queryBoardState() {
        if (isCheck()) {
            if (noValidMoves()) {
                return BoardState.CHECKMATE;
            }
            return BoardState.CHECK;
        }
        if (noValidMoves()) {
            return BoardState.STALEMATE;
        }

        return BoardState.DEFAULT;
    }

    public boolean isCheck() {
        attackMap.calculate(this, this.getEnemyTeam());
        return attackMap.getTile(getCurrentTeam().getKing().getCoOrds());
    }

    public boolean noValidMoves() {
        Log.info("All pieces: ");
        for (Piece piece : getCurrentTeam().getPieceList()) {
            Log.append(piece.getName());
        }
        for (Piece piece : getCurrentTeam().getPieceList()) {
            Log.debug("GameBoardContainer", "Getting validated moves for " + piece.getName());
            List<Move> moveList = getValidatedMoves(piece.moves(this));


            if (!moveList.isEmpty()) {
                Log.error("MoveList", "it was " + piece.getTeam().getName() + piece.getName() +
                        " on " + posString(piece.getCoOrds()));
                for (Move move : moveList) {
                    Log.append(longAlgebraicNotation(move));
                }
                return false;


            }
        }
        return true;
    }



    public void doMoveWithValidation(String longAlgebraicNotation) {
        doMoveWithValidation(pos(longAlgebraicNotation.substring(0, 2)), pos(longAlgebraicNotation.substring(3)));
    }
}
