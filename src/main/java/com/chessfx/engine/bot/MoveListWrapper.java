package com.chessfx.engine.bot;

import com.chessfx.engine.moves.Move;
import com.chessfx.log.Log;

import java.util.ArrayList;
import java.util.List;

public class MoveListWrapper {
    private final List<Move> ls = new ArrayList<>();

    public List<Move> getList() {
        return ls;
    }

    public void add(Move mv) {
        Log.debug("MoveListWrapper", "Added move to List.");
        ls.add(mv);
    }

    public void clear() {
        ls.clear();
    }

    public boolean isEmpty() {
        return ls.isEmpty();
    }
}
