package com.chessfx.engine.pieces;

import com.chessfx.log.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.chessfx.engine.pieces.PieceAttribute.*;

public class PieceDataManager {

    // these are passed around via chessConfig
    private final Map<String, PieceData> pieceMap;
    private final Map<String, String> fenIdToNameMap;


    public PieceDataManager() {
        pieceMap = new HashMap<>();
        fenIdToNameMap = new HashMap<>();

        String csv;
        try {
            csv = new String(Objects.requireNonNull(getClass().getResourceAsStream(
                    "pieces.csv")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            csv = null;
            Log.error("PieceDataManager", "Pieces.csv could not load. " +
                    "Application cannot run.");
            throw new RuntimeException(e);
        }
        String[] stringArray = csv.split("\n");
        for (int i = 0; i < stringArray.length; i++) {
            String[] pieceData = stringArray[i].split(",");
            Log.debug("PieceDataManager", "Split string as array: " + Arrays.toString(pieceData));

            // todo, use i as a unique id...
            pieceMap.put(pieceData[NAME.ordinal()], new PieceData(pieceData)); // map of name to pieceData
            fenIdToNameMap.put(pieceData[FEN_ID.ordinal()], pieceData[NAME.ordinal()]); // map of fenId to name
        }
    }

    /**
     * @return a map of the string pieceName against the PieceData class.
     */
    public Map<String, PieceData> getPieceDataMap() {
        return pieceMap;
    }

    public Map<String, String> getFenIdToNameMap() {
        return fenIdToNameMap;
    }
}
