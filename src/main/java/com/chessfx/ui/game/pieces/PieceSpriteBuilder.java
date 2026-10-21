package com.chessfx.ui.game.pieces;

import com.chessfx.engine.board.Team;
import javafx.util.Builder;
import org.girod.javafx.svgimage.*;

public class PieceSpriteBuilder implements Builder<SVGImageRegion> {
    SVGImageRegion svgImageRegion;

    public PieceSpriteBuilder(String svgString, Team team) {
        /// Working with the fxsvgimage library;
        svgImageRegion = SVGLoader.load(svgString).createRegion();
        svgImageRegion.getStyleClass().add(team.getStyleClassString());
        svgImageRegion.getStyleClass().add("whole-piece");
    }

    @Override
    public SVGImageRegion build() {
        return svgImageRegion;
    }
}
