package com.chessfx.ui.game.pieces;

import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;
import com.chessfx.ui.game.board.BoardUIModel;
import javafx.scene.layout.Region;

import java.io.IOException;
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
     * @param pieceNameSet set of all the unique piece identifiers which are in a chess game
     */
    public PieceSpriteFactory(Set<String> pieceNameSet) {
        svgStringMap = new HashMap<>();
        for (String name : pieceNameSet) {
            svgStringMap.put(name, getSVGFileAsString(name));
        }
    }


    private String getSVGFileAsString(String name) {
        String svgContent = null;
        // first check resources
        try {
            svgContent = new String(Objects.requireNonNull(getClass().getResourceAsStream(
                    name + ".svg")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (NullPointerException e) {
            Log.error("PieceImageController", "path at piece identifier was null");
            Log.cont("Running as placeholder.");
            return getSVGFileAsString("placeholder");
        } catch (IOException e) {
            Log.error("PieceImageController", "IO Exception");
            e.printStackTrace();
        }

        // then check user config or other way around (haven't implemented yet)
        return svgContent;
    }


    public Region makePieceSprite(TileBase tile) {
        return makePieceSprite(tile.getPiece());
    }

    public Region makePieceSprite(Piece piece) {
        String svgFileString = svgStringMap.get(piece.getName());
        PieceSpriteBuilder sprite = new PieceSpriteBuilder(svgFileString, piece.getTeam());
        return sprite.build();
    }

}