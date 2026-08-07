package com.chess.chessfx.gameui.pieces;

import com.chess.chessfx.engine.pieces.Team;
import javafx.scene.Node;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import org.girod.javafx.svgimage.SVGImage;
import org.girod.javafx.svgimage.SVGLoader;

public class PieceSprite {
    Team team;
    SVGImage svgImage;

    public PieceSprite(String svgString, Team team) {
        this.team = team;
        svgImage = SVGLoader.load(svgString);
//        svgImage.
    }

    public Node getAsNode() {
        return svgImage;
    }
}
