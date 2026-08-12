package com.chess.chessfx.ui.game.pieces;

import com.chess.chessfx.engine.pieces.Team;
import javafx.util.Builder;
import org.girod.javafx.svgimage.*;

public class PieceSpriteBuilder implements Builder<SVGImageRegion> {
    Team team;
    SVGImageRegion svgImageRegion;

    public PieceSpriteBuilder(String svgString, Team team) {

        // Working with new fxsvgimage library;
        svgImageRegion = SVGLoader.load(svgString).createRegion();
        svgImageRegion.getStyleClass().add(team.getStyleClassString());
        svgImageRegion.getStyleClass().add("whole-piece");
    }



    @Override
    public SVGImageRegion build() {
        return svgImageRegion;
    }
}
