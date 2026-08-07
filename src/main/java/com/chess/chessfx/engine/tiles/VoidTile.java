package com.chess.chessfx.engine.tiles;

import com.chess.chessfx.gameui.tiles.TileUI;

public class VoidTile extends BarrierTile {

    @Override
    public void style(TileUI tileUI) {
        tileUI.getStyleClass().add("void-tile");
    }


}
