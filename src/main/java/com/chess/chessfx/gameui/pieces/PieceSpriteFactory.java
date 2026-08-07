package com.chess.chessfx.gameui.pieces;

import com.chess.chessfx.engine.pieces.Piece;
import com.chess.chessfx.engine.pieces.Team;
import javafx.scene.Node;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PieceSpriteFactory {

    Map<String, String> svgStringMap;

    /**
     * This controller uses my attempt at 'lazy loading' where it loads all
     * the needed piece SVG paths into memory from external files so that the SVG nodes
     * can be created without constantly reading from external files.
     *
     * @param pieceIdSet comma separated values of all the piece identifiers
     *                   which must be rendered on the board.
     */
    public PieceSpriteFactory(Set<String> pieceIdSet) {
        // load all necessary sprites by going through
        // each file name and adding to list? Or hashmap
        svgStringMap = new HashMap<>();
        for (String id : pieceIdSet) {
            svgStringMap.put(id, getSVGPathString(id));
        }
    }

    // https://www.tutorialspoint.com/javafx/2dshapes_svgpath.htm

    private String getSVGPathString(String identifier) {
        String svgString = null;

        // first check resources
        try {
            InputStream inputStream = getClass().getResourceAsStream(identifier + ".txt");
            svgString = inputStream.toString();
        } catch (NullPointerException e) {
            System.out.println("PieceImageController: path at piece identifier was null");
        }

        // then check user config

        return svgString;
    }


    /**
     * Twins with
     *
     * @param piece
     * @return
     */
    public Node makePieceSprite(Piece piece) {
        // parse data from file based on identifier.

        String path = svgStringMap.getOrDefault(piece.getIdentifier(), "");
        Team team = piece.getTeam();

        PieceSprite sprite = new PieceSprite(path, team);

        return sprite.getAsNode();
    }
}
