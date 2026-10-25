package com.chessfx.engine.board;

import com.chessfx.engine.bot.AttackMap;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.King;
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

        attackMap.calculate(virtualBoard, virtualBoard.getEnemyTeam());
        setBoardState(queryBoardState());

        if (getCurrentTeam().isBot()) {
            takeTurnAsBot();
        }
    }

    @Override
    protected void undoUpdateGame() {
        super.undoUpdateGame();
        attackMap.calculate(virtualBoard);
        setBoardState(queryBoardState());
    }

    /**
     * Called after class is fully constructed.
     */
    public void gameStart() {
        // Initial game update:


        virtualBoard = makeIsolatedBoardContainer();

        attackMap.calculate(virtualBoard);

        setBoardState(queryBoardState());
    }

    private BoardContainer makeIsolatedBoardContainer() {
        List<Team> teamList = gameModel.copyTeams();
        TileBase[][] board = boardCopy(this.board, teamList);
        GameStateModel gameStateModel = new GameStateModel(gameModel, teamList);

        BoardContainer isolatedBoardContainer = new IsolatedBoardContainer(gameStateModel, board);
        return isolatedBoardContainer;
    }

    public TileBase[][] boardCopy(TileBase[][] board, List<Team> teamList) {
        int m = board.length, n = board[0].length;

        TileBase[][] copy = new TileBase[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                TileBase tile = board[i][j];
                if (tile.hasPiece()) {

                    Piece piece = tile.getPiece();
                    int teamIdx = piece.getTeam().idx();
                    Team team = teamList.get(teamIdx);

                    copy[i][j] = tile.copy(team, gameModel.getChessConfig().getPieceFactory());
                    Piece copyPiece = copy[i][j].getPiece();
                    if (copyPiece instanceof King) {
                        Log.debug("King found...");
                        team.setKing((King) copyPiece);
                    }
                    team.getPieceList().add(copyPiece);
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
        super.doMove(move);
        virtualBoard.doMove(move);
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
            Log.debug("VirtualBoard representation:\n", virtualBoard.getStringRepresentation());
            virtualBoard.doMove(move);
            Log.debug("VirtualBoard representation:\n", virtualBoard.getStringRepresentation());

            attackMap.calculate(virtualBoard, virtualBoard.getCurrentTeam());

            Log.debug("AttackMap", attackMap.logOutputStringRepresentation());
            if (!isCheck(virtualBoard, virtualBoard.getEnemyTeam())) {
                /// not check, therefore move is valid
                validated.add(move);
            }


            virtualBoard.undoLastMove();
            Log.debug("VirtualBoard representation:\n", virtualBoard.getStringRepresentation());

        }


        return validated;
    }

    protected void setBoardState(BoardState boardState) {
        Log.info("BoardState is " + boardState.getText());
        gameModel.setBoardState(boardState);
    }


    @Override
    protected void captureListUpdate(Piece enemy) {
        getCurrentTeam().getCapturedPieces().add(enemy);
        gameModel.getEnemyTeam().getPieceList().remove(enemy);
    }

    @Override
    protected void undoCaptureListUpdate(Piece enemy) {
        getCurrentTeam().getCapturedPieces().remove(enemy);
        gameModel.getEnemyTeam().getPieceList().add(enemy);
    }


    private boolean isCheck(BoardContainer board, Team team) {
        return attackMap.getTile(team.getKing().getCoOrds());
    }

    public void terminate() {
        // do nothing. Equivalent to an abstract method
    }


    public AttackMap getAttackMap() {
        return attackMap;
    }

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
        attackMap.calculate(this);
        return attackMap.getTile(getCurrentTeam().getKing().getCoOrds());
    }

    public boolean noValidMoves() {
        return false;
    }

}
