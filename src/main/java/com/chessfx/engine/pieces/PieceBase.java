package com.chessfx.engine.pieces;

import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.moves.MoveVector;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;

abstract public class PieceBase {

    final protected String name;
    final protected String fenId;
    final protected int value;
    final protected Team team;

    protected Coordinate coOrds;
    protected boolean hasMoved;

    public PieceBase(String name, String fenId, int value, Team team, Coordinate coOrds) {
        this.name = name;
        this.fenId = fenId;
        this.team = team;
        this.value = value;

        this.coOrds = coOrds;


    }
}
