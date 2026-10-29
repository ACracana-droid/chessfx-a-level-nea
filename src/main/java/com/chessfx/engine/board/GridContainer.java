package com.chessfx.engine.board;

import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.tiles.TileBase;

public interface GridContainer<T> {

    T getTile(Coordinate coOrds);

    T[][] getBoard();

    default T getTileFrom(T[][] array, Coordinate coOrds) {
        return array[coOrds.y()][coOrds.x()];
    }
}
