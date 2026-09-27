package com.chessfx.ui.game.tiles;

import com.chessfx.engine.tiles.VoidTile;


public class DevTileUI extends TileUI {

    public DevTileUI() {
        super(new VoidTile());
    }

    @Override
    public String toString() {
        return super.toString() + "[DEV TILE]";
    }
}
