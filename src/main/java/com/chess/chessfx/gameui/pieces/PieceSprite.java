package com.chess.chessfx.gameui.pieces;

import com.chess.chessfx.engine.pieces.Team;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Scale;
import org.girod.javafx.svgimage.*;

public class PieceSprite {
    Team team;
    SVGImageRegion svgImageRegion;

    public PieceSprite(String svgString, Team team) {
        this.team = team;

        // Working with new fxsvgimage library;
        svgImageRegion = SVGLoader.load(svgString).createRegion();
        svgImageRegion.getStyleClass().add(team.getStyleClassString());
        svgImageRegion.getStyleClass().add("whole-piece");
    }

    public Node getAsNode() {
        return svgImageRegion;
    }
}
