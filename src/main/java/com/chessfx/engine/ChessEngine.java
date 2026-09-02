package com.chessfx.engine;

import com.chessfx.engine.pieces.King;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileParent;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChessEngine {

    public static boolean givesCheck(TileParent[][] board, int y, int x) {

        return false;
    }

//    private static BoardState getBoardState(List<Piece> pieceList, BoardContainer board) {
//        List<Move> validMoves = new ArrayList<>();
//        for (Piece piece : pieceList) {
//            validMoves.addAll(validateMoves(piece.moves(board)));
//        }
//        if (validMoves.isEmpty()) {
//            if (givesCheck(enemyTeam.pieces, getAlliedTeam().getKing(), board))
//                return BoardState.STALEMATE;
//            else return BoardState.CHECKMATE;
//        }
//        if (!isNotThreatToKing(enemyTeam.pieces, getAlliedTeam().getKing(), board)) {
//            return BoardState.CHECK;
//        }
//
//        return BoardState.DEFAULT;
//    }

    private static List<Move> validateMoves(List<Move> moves) {
        for (Move move : moves) {

        }
        return moves;
    }

    private static TileParent getTile(int[] coOrds, TileParent[][] board) {
        return board[coOrds[0]][coOrds[1]];
    }


    // TODO: DOCUMENT Fix turns
    public static BoardState getBoardState(GameStateModel gameModel, TileParent[][] board) {

        if (true)
            return BoardState.DEFAULT;


        King king = gameModel.getCurrentTeam().getKing();
        List<Piece> enemies = gameModel.getNextTeam().getPieceList();
        if (underThreat(king, gameModel.getCurrentTeam().getMoveSets(), gameModel.getInstantiatedPieceNames(), board)) {
            if (checkmate(king, enemies, board)) {
                return BoardState.CHECKMATE;
            }
            return BoardState.CHECK;
        }

        if (cannotMove(gameModel.getCurrentTeam().getPieceList())) {
            return BoardState.STALEMATE;
        }

        return BoardState.DEFAULT;
    }

    private static boolean cannotMove(List<Piece> pieceList) {

        return false;
    }

    private static boolean checkmate(King king, List<Piece> enemies, TileParent[][] board) {


        return false;
    }

    // This is quite messy. This whole class is a major work in progress.
    private static boolean underThreat(King king,
                                       Map<String, Set<MoveVector>> moveSets,
                                       Set<String> instantiatedPieceNames, TileParent[][] board) {
        int[] kingCoOrds = king.getCoOrds();
        Set<?> set = moveSets.get("k");
        for (String name : instantiatedPieceNames) {
            for (MoveVector moves : moveSets.get(name)) {
//
//                if (inspectTile(new Move(kingCoOrds[0],kingCoOrds[1], ))) {
//
//                }
            }
        }

        // check each move variant from king square


        return false;
    }
}
