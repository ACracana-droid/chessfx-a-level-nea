package com.chess.chessfx.engine.pieces;

import com.chess.chessfx.engine.Move;
import com.chess.chessfx.engine.tiles.TileParent;
import com.chess.chessfx.gameui.pieces.PieceSpriteFactory;

import java.util.ArrayList;
import java.util.List;

abstract public class Piece {

    Team team;
    int[] coOrds;
    String identifier;

    PieceSpriteFactory imageController;

    public Piece(Team team, int rank, int file) {
        this.team = team;
        coOrds = new int[]{rank, file};
        team.getPieceList().add(this);
    }

    public char getAscii() {
        return 'a';
    }

    protected List<Move> infiniteDistancePieceMove(TileParent[][] board, int[][] moveVectors) {
        List<Move> ls = new ArrayList<>();
        for (int[] vector : moveVectors) {
            int rank = coOrds[0] + vector[0], file = coOrds[1] + vector[1];
            int xLimit = vector[0] == 1 ? board.length : -1;
            int yLimit = vector[1] == 1 ? board[0].length : -1;
            while (rank != xLimit && file != yLimit) {
                if (board[rank][file].hasPiece()) break;
                ls.add(new Move(rank, file));
                rank += vector[0];
                file += vector[1];
            }
            if (rank != xLimit && file != yLimit) {
                if (board[rank][file].isPossibleMove(team)) {
                    ls.add(new Move(rank, file));
                } // otherwise it is an ally and a move is not allowed.
            }
        }
        return ls;
    }

    public Team getTeam() {
        return team;
    }

    public String getIdentifier() {
        return identifier;
    }
}
