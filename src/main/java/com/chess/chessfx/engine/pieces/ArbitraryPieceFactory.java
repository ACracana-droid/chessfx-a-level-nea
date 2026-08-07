package com.chess.chessfx.engine.pieces;

import com.chess.chessfx.engine.MoveVector;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ArbitraryPieceFactory {

    Map<String, PieceData> cache;

    public ArbitraryPieceFactory() {
    }

    public Piece create(String id, Team team, int rank, int file) {
        PieceData pieceData = getPieceEngineData(id);
        // now to deal with pieceData.
        return new ArbitraryPiece(id, team, rank, file);
    }

    //TODO: GET THIS UP AND RUNNING!
    private PieceData getPieceEngineData(String identifier) {
        // first check cache, then resources, then the app data folder.

        if (cache.containsKey(identifier)) {
            return cache.get(identifier);
        }

        try {
            Properties properties;
            InputStream inputStream = getClass().getResourceAsStream(identifier + ".properties");
            properties = new Properties();
            properties.load(inputStream);

            String importMoveString = properties.getProperty("import");

            String moveVectorsString = properties.getProperty("moveVectors");
            PieceData data = new PieceData(moveVectorsString);

            if (importMoveString != null) {

                // If pieces import each other in a cycle, this will cause stack overflow.
                // This should be fine in development, but users should not be able to
                // import a piece into itself, for example.
                String[] importPieceId = importMoveString.split(" ");
                PieceData[] importPieceData = new PieceData[importPieceId.length];
                for (int i = 0; i < importPieceId.length; i++) {
                    importPieceData[i] = getPieceEngineData(importPieceId[i]);
                }
                data.merge(importPieceData);
            }
            return data;
        } catch (IOException e) {
            System.out.println("ArbitraryPiece Factory: There either is not a file containing information about this piece" +
                    "\nor it could not be found.");
            return new PieceData("1,0");
        }

    }


    /**
     * Simple class for containing information.
     */
    private class PieceData {

        // each move contains data on whether it is 'flying' or other properties
        MoveVector[][] moveVectors;

        public PieceData(String moveVectorString) {
            List<int[]> ls = new ArrayList<>();
            int[] vector = new int[2];
            int pointer = 0;
            int startOfNum = 0;
            for (int i = 0; i < moveVectorString.length(); i++) {
                if (moveVectorString.charAt(i) == ' ') {
                    ls.add(vector);
                } else if (moveVectorString.charAt(i) == ',') {
                    vector[pointer++] = Integer.parseInt(moveVectorString.substring(startOfNum, i));
                } else {
                    continue;
                }
                startOfNum = i + 1;
            }
            ls.add(vector);
        }

        public void merge(PieceData[] importPieceData) {
            // TODO: THIS!
        }
    }

//    public List<Move> getFlyingLimitedMoves(TileParent[][] board, int i, int j,) {
//        List<Move> ls = new ArrayList<>();
//
//        for (int[] move :) {
//            int rank = i + move[0];
//            int file = j + move[1];
//            if (isValidCoOrd(rank, file, team))
//                ls.add(new Move(rank, file)); // valid location.
//        }
//
//        return ls;
//    }
}
