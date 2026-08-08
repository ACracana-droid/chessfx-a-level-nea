package com.chess.chessfx.gameui.pieces;

import com.chess.chessfx.engine.pieces.Piece;
import com.chess.chessfx.engine.pieces.Team;
import com.chess.chessfx.engine.tiles.TileParent;
import com.chess.chessfx.gameui.tiles.TileUI;
import javafx.scene.Node;
import javafx.scene.transform.Scale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PieceSpriteFactory {

    Map<String, String> svgStringMap;

    /**
     * This constructor is my attempt at 'lazy loading' where all
     * the needed piece SVG data is loaded into memory from external files so that
     * SVG nodes, particularly duplicates can be created without constantly
     * reading from external files.
     *
     * @param pieceIdSet set of all the unique piece identifiers which are in a chess game
     */
    public PieceSpriteFactory(Set<String> pieceIdSet) {
        svgStringMap = new HashMap<>();
        for (String id : pieceIdSet) {
            svgStringMap.put(id, getSVGFileAsStream(id));
        }
    }


    private String getSVGFileAsStream(String identifier) {
        String svgContent = null;
        // first check resources
        try {
            svgContent = new String(Objects.requireNonNull(getClass().getResourceAsStream(
                    identifier + ".svg")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (NullPointerException e) {
            System.out.println("PieceImageController: path at piece identifier was null");
            System.out.println("Running as placeholder.");
            return getSVGFileAsStream("placeholder");
        } catch (IOException e) {
            System.out.println("PieceImageController: IO Exception");
            e.printStackTrace();
        }

        // then check user config or other way around (haven't implemented yet)
        return svgContent;
    }


    public Node makePieceSprite(TileParent tile) {
        Piece piece = tile.getPiece();

        String svgFileString = svgStringMap.get(piece.getIdentifier());
        PieceSprite sprite = new PieceSprite(svgFileString, piece.getTeam());
        return sprite.getAsNode();
    }
}