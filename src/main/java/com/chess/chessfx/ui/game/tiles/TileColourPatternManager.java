package com.chess.chessfx.ui.game.tiles;


public class TileColourPatternManager {

    int colourNum;

    /**
     * @param tileCheckerboardOffset property determining the offset of the checkerboard pattern
     *                               from the usual pattern. At the default value (0) A1 is a dark tile.
     */
    public TileColourPatternManager(int tileCheckerboardOffset) {
        if (tileCheckerboardOffset == 0) {
            colourNum = 0;
        } else {
            colourNum = 1;
        }
    }

    public TileColour getCycle() {
        if (colourNum == 1) {
            colourNum = 0;
            return TileColour.DARK;
        } else {
            colourNum = 1;
            return TileColour.LIGHT;
        }
    }

    public void skipCycle() {
        colourNum = (colourNum + 1) % 2;
    }
}
