package com.chess.chessfx.gameui;

public enum TileColour {
    LIGHT,
    DARK,
    SPECIAL_1,
    SPECIAL_2;

    public TileColour cycle() {
        if (this.equals(LIGHT)) return DARK;
        else return LIGHT;
    }
}
