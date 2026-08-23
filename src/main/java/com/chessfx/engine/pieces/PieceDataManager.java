package com.chessfx.engine.pieces;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PieceDataManager {

    private String csv;

    // these are passed around via chessConfig
    private final Map<String, PieceData> pieceMap;
    private final Map<String, String> fenIdToNameMap;

    public PieceDataManager() {
        pieceMap = new HashMap<>();
        fenIdToNameMap = new HashMap<>();

        try {
            csv = new String(Objects.requireNonNull(getClass().getResourceAsStream(
                    "pieces.csv")).readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            csv = null;
            System.out.println("PieceDataManager: Pieces.csv could not load. " +
                    "Application cannot run.");
            throw new RuntimeException(e);
        }
        for (String piece : csv.split("\n")) {
            System.out.println("PieceDataManager: raw string from pieces.csv: " + piece);
            String[] pieceData = piece.split(",");
            System.out.println("PieceDataManager: split string as array: " + piece);

            pieceMap.put(pieceData[0], new PieceData(pieceData)); // map of name to pieceData
            fenIdToNameMap.put(pieceData[1], pieceData[0]); // map of fenId to name
        }
    }

    public Map<String, PieceData> getPieceDataMap() {
        return pieceMap;
    }

    public Map<String, String> getFenIdToNameMap() {
        return fenIdToNameMap;
    }
}
