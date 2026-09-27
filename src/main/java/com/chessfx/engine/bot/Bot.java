package com.chessfx.engine.bot;

import com.chessfx.engine.board.*;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.moves.MoveVector;
import com.chessfx.engine.pieces.King;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.pieces.PieceData;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;

import java.util.List;
import java.util.Map;

public class Bot extends BoardContainerBase {

    private final GameStateModel gameModel;


//    private final List<Move>[][] boardMap;


    public Bot(GameStateModel gameModel, TileBase[][] board) {
        super(board);
        // Bot has own copy of gameModel and board to manipulate as it sees fit.
        this.gameModel = gameModel;
    }

    public static BoardState queryBoardState(BoardContainer boardContainer) {
        if (isCheck(boardContainer)) {
            if (isCheckmate(boardContainer)) {
                return BoardState.CHECKMATE;
            }
            return BoardState.CHECK;
        }

        return BoardState.DEFAULT;
    }

    private static boolean isCheckmate(BoardContainer boardContainer) {
        // TODO: Implement
        return false;
    }


    // TODO: use a true 'board container' class which adds some utility such as using coords for moves.
    private boolean isKingThreatenedBasic(GameStateModel gameModel, TileBase[][] board) {
        Team currentTeam = gameModel.getCurrentTeam();
        King king = currentTeam.getKing();
        int[] coOrds = king.getCoOrds();

        Map<String, PieceData> pieceDataMap = gameModel.getChessConfig().getPieceDataMap();

        // Gets each instantiated piece in the game.
        // Gets each move from each piece.
        // If there is a move from the king to that piece,
        // then the king must be under threat.
        // This only works for standard pieces,
        // not those like, say, a piece in checkers
        for (String pieceName : gameModel.getInstantiatedPieceNames()) {
            for (MoveVector moveVector : pieceDataMap.get(pieceName)
                    .getAllMoveVectors(currentTeam)) {
                int destY = coOrds[0] + moveVector.getDy();
                int destX = coOrds[1] + moveVector.getDx();
                if (board[destY][destX].hasPiece(pieceName, currentTeam)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isCheck(BoardContainer boardContainer) {
        // TODO: MAKE CHECK WORK!!
        GameStateModel gameModel = boardContainer.getGameModel();
        Team currentTeam = gameModel.getCurrentTeam();
//        Log.debug("Current team is " + currentTeam.getName());
        // Must search for check on the current team.
        King king = currentTeam.getKing();
        int[] kingCoOrds = king.getCoOrds();

        Team enemyTeam = gameModel.getNextTeam();
        List<Piece> enemies = enemyTeam.getPieceList();

        for (Piece enemy : enemies) {
            for (Move mv : enemy.captures(boardContainer)) {
                if (compareDestination(mv, kingCoOrds)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean compareDestination(Move mv, int[] kingCoOrds) {
        return mv.getEndY() == kingCoOrds[0] && mv.getEndX() == kingCoOrds[1];
    }


    public double minimax(int depth) {
        if (depth == 0) return evaluate();
        return 0.0;
    }

    private double evaluate() {
        // TODO: BOARD EVAL.

        return 0.0;
    }

    /**
     * Scraped method for maintaining a grid of move lists with references to each piece that the
     *
     * @param boardContainer
     * @param board
     * @param teams
     * @return
     */
    public MoveListWrapper[][] makeMoveBoard(BoardContainer boardContainer, TileBase[][] board, List<Team> teams) {
        if (board.length < 1) return new MoveListWrapper[][]{};

        int n = board.length;
        int m = board[0].length;
        MoveListWrapper[][] moveBoard = initialiseMoveBoard(n, m);

        Log.debug("Making 'moveBoard'");
        for (Team team : teams) {
            Log.debug("LOOP TEAM");
            for (Piece p : team.getPieceList()) {
                Log.debug("LOOP PIECE");
                List<Move> moves = p.moves(boardContainer);
                if (moves == null) {
                    Log.warn("Piece moves for " + p + " is null...");
                    continue;
                }
                for (Move mv : p.moves(boardContainer)) {
                    Log.debug("LOOP MOVE");
                    if (mv == null) {
                        Log.debug("Move from moves is null.");
                        continue;
                    }
                    Log.warn("Before end: move is " + mv);
                    moveBoard[mv.getEndY()][mv.getEndX()].add(mv);
                    Log.debug("End of loop");
                }
            }
        }
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < m; j++) {
//                moveBoard[i][j] = board[i][j];
//            }
//        }
        return moveBoard;
    }

    private MoveListWrapper[][] initialiseMoveBoard(int n, int m) {
        MoveListWrapper[][] moveBoard = new MoveListWrapper[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                moveBoard[i][j] = new MoveListWrapper();
            }
        }
        return moveBoard;
    }
}
