package com.chessfx.engine.board;

import com.chessfx.engine.tiles.TileBase;

import java.util.ArrayList;
import java.util.List;

public class IsolatedBoardContainer extends BoardContainer {


    public IsolatedBoardContainer(GameStateModel gameModel, TileBase[][] board) {
        super(gameModel, board);
    }


    @Override
    public Team getEnemyTeam() {
        return super.getEnemyTeam();
    }

    @Override
    public Team getCurrentTeam() {
        return super.getCurrentTeam();
    }

}
