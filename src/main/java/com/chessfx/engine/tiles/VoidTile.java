package com.chessfx.engine.tiles;

import com.chessfx.ui.game.tiles.TileUI;

public class VoidTile extends BarrierTile {

    @Override
    public void style(TileUI tileUI) {
        tileUI.getStyleClass().add("void-tile");
    }

    public VoidTile() {
        super();
    }
}
