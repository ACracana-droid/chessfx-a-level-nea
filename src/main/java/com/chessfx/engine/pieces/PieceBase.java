package com.chessfx.engine.pieces;

import com.chessfx.engine.board.Team;
import com.chessfx.engine.moves.Coordinate;

abstract public class PieceBase {

    final protected String name;
    final protected String fenId;
    final protected int value;
    final protected Team team;

    protected Coordinate pos;
    protected boolean hasMoved;

    public PieceBase(String name, String fenId, int value, Team team, Coordinate pos) {
        this.name = name;
        this.fenId = fenId;
        this.team = team;
        this.value = value;
        this.pos = pos;

    }

    public String getFenID() {
        return fenId;
    }

    public Team getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

}
