package com.chessfx.engine.pieces;

import com.chessfx.engine.board.GameStateModel;
import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.log.Log;

import java.util.List;

public class Pawn extends Piece {
    public Pawn(Team team, int rank, int file, boolean hasMoved, PieceData pieceData) {
        super("pawn", team, rank, file, hasMoved, pieceData);
    }

    @Override
    protected boolean isPromotion(int y) {
        return game.atEndRank(y);
    }

    //TODO ENPASSANT
    @Override
    protected List<Move> calculateMoves() {
        List<Move> ls = super.calculateMoves();
        // need piece that can enpassant and piece that can be 'enpassant-ed' in same rank.
        // not hardcoding ranks, would be easy though.

        // coordinate is based off dy from pieces.csv. The two diagonal moves must be first and second.
        Coordinate[] places = new Coordinate[2];
        Log.debug("starting loop");
        for (int i = 0; i < 2; i++) {
            places[i] = new Coordinate(pos.y() + moveSet[i + 1].getDy(), pos.x() + moveSet[i + 1].getDx());
            if (enPassantTargetSquareViable(places[i])) {
                Log.debug("adding move");
                ls.add(Move.make(this, places[i].y(), places[i].x(), List.of(places[i])));
            }
            Log.debug("in at end of" +
                    " loop");
        }
        Log.debug("ending loop");

//        for (int num : nums) {
//            if (checkPiecesInSameRank(num)) {
//                Piece capture = game.getTile(coOrds.y(), num).getPiece();
//                ls.add(Move.make(this, coOrds.y(), num, List.of(capture)));
//            }
//        }

        return ls;
    }


    private boolean enPassantTargetSquareViable(Coordinate pos) {
        GameStateModel gameModel = game.getGameModel();

        if (gameModel.getEnPassantTarget() != null) {
            return gameModel.getEnPassantTarget().equals(pos);
        }
        return false;
    }


//    private boolean checkPiecesInSameRank(int x) {
//        return game.inBoardBounds(coOrds.y(), x)
//                && game.getTile(coOrds.y(), x).hasPiece("pawn")
//                && enPassantTargetSquareViable();
//    }


//    private boolean lastMoveWasDoubleStepTo(int y, int x) {
//        if (game.getPlyStack().isEmpty()) return false;
//        PlyData ply = game.getPlyStack().peek();
//        int dy = Math.abs(ply.move().getEndY() - ply.move().getStartY());
//        return dy == 2 && ply.move().destCoOrds().isSame(y, x);
//    }
}
