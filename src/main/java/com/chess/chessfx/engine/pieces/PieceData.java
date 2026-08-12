package com.chess.chessfx.engine.pieces;

import com.chess.chessfx.engine.MoveVector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class PieceData {

    String name;
    String fenId;
    int value;
    // using a 'set' because each move is unique.
    Set<MoveVector> set;

    public PieceData(String[] data) {
        name = data[0];
        fenId = data[1];
        value = Integer.parseInt(data[2]);
        makeMoveSetFromData(data[3]);
        System.out.println("PieceData: All moves for " + name + ":" + set);
    }

    private void makeMoveSetFromData(String moveData) {
        System.out.println("Initial move data: " + moveData);
        set = new HashSet<>();
        if (moveData.charAt(0) == '"' && moveData.charAt(moveData.length() - 1) == '"') {
            moveData = moveData.substring(1, moveData.length() - 1);
            System.out.println("Trimming speech marks: " + moveData);
        }

        for (String move : moveData.split(";")) {
            parseParlettNotation(move);
        }
    }

    private final static char[] CONDITION_AND_TYPE_CHARS = new char[]{
            // conditions
            'i', 'c', 'o', // initial, capture, not capture
            // types
            '~', '^' // isLeaping, isHopping
    };
    private final static String[] DIRECTION_CODES = new String[]{
            "+",   // orthogonal
            ">",   // orthogonally forwards
            "<",   // orthogonally backwards
            "<>",  // orthogonally forwards and backwards
            "=",   // orthogonally sideways
            ">=",  // orthogonally forwards and sideways
            "<=",  // orthogonally backwards and sideways
            "X",   // diagonally
            "X>",  // diagonally forwards
            "X<",  // diagonally backwards
            "*"    // orthogonal and diagonal
    };

    private int i;
    private boolean[] flags;

    private void parseParlettNotation(String move) {

        // conditions, type, distance, direction, other
        i = 0;
        flags = new boolean[CONDITION_AND_TYPE_CHARS.length];
        int len = move.length();

        processConditionsAndMoveTypes(move); // handles flags

        int distance = getSteps(move, len, -1);
        System.out.println("PieceData: distance is " + distance);

        if (move.charAt(i) == '/') {
            int vector1 = distance;
            int vector2;
            i++;
            int j = i;
            while (i < len && Character.isDigit(move.charAt(i))) {
                i++;
            }
            vector2 = Integer.parseInt(move.substring(j, i));
            makeMovesFromHippogonalVectors(vector1, vector2, 1); // does not support nightrider
        } else {
            String direction = getDirectionCode(move, len);
            i += direction.length();
            makeMovesFromStandardDirections(distance, direction);
        }
    }


    private void processConditionsAndMoveTypes(String move) {
        boolean[] presenceCheck = new boolean[256]; // all chars are within extended ascii.
        while (isConditionOrTypeChar(move.charAt(i), presenceCheck)) i++;
        int j = 0;
        for (char c : CONDITION_AND_TYPE_CHARS) {
            flags[j++] = presenceCheck[c];
        }
    }

    private boolean isConditionOrTypeChar(char ch, boolean[] presenceCheck) {
        for (char c : CONDITION_AND_TYPE_CHARS) {
            if (ch == c) {
                presenceCheck[ch] = true;
                return true;
            }
        }
        return false;
    }

    private int getSteps(String move, int len, int steps) {
        if (move.charAt(i) != 'n') {
            int j = i;
            while (i < len && Character.isDigit(move.charAt(i))) {
                i++;
            }
            steps = Integer.parseInt(move.substring(j, i));
        } else {
            i++;
        }
        return steps;
    }

    private void makeMovesFromHippogonalVectors(int dy, int dx, int steps) {
        createMV(new int[][]{
                {dy, dx},
                {dy, -dx},
                {-dy, dx},
                {-dy, -dx},
                {dx, dy},
                {dx, -dy},
                {-dx, dy},
                {-dx, -dy}
        }, steps);
    }

    private void createMV(int[][] vectors, int steps) {
        for (int[] vector : vectors) {
            set.add(new MoveVector(vector, flags, steps));
        }
    }

    private void makeMovesFromStandardDirections(int distance, String direction) {
        // y and x, rank and file
        switch (direction) {
            case "+":
                // Orthogonal
                createMV(new int[][]{{0, 1}, {1, 0},
                        {0, -1}, {-1, 0}}, distance);
                break;

            case ">":
                // Orthogonally forwards
                createMV(new int[][]{{1, 0}}, distance);
                break;

            case "<":
                // Orthogonally backwards
                createMV(new int[][]{{-1, 0}}, distance);
                break;

            case "<>":
                // Orthogonally forwards and backwards
                createMV(new int[][]{{1, 0}, {-1, 0}}, distance);
                break;

            case "=":
                // Orthogonally sideways
                createMV(new int[][]{{0, 1}, {0, -1}}, distance);
                break;

            case ">=":
                // Orthogonally forwards or sideways
                createMV(new int[][]{{1, 0}, {0, 1}, {0, -1}}, distance);
                break;

            case "<=":
                // Orthogonally backwards or sideways
                createMV(new int[][]{{-1, 0}, {0, 1}, {0, -1}}, distance);
                break;

            case "X":
                // Diagonal
                createMV(new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}, distance);
                break;

            case "X>":
                // Diagonally forwards
                createMV(new int[][]{{1, 1}, {1, -1}}, distance);
                break;

            case "X<":
                // Diagonally backwards
                createMV(new int[][]{{-1, 1}, {-1, -1}}, distance);
                break;

            case "*":
                // All 8 directions
                createMV(new int[][]{
                        {0, 1}, {1, 0}, {0, -1}, {-1, 0},
                        {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
                }, distance);
                break;

            default:
                throw new IllegalArgumentException("Unknown direction: " + direction);
        }

    }

    private String getDirectionCode(String move, int len) {
        String direction;
        if (i + 1 >= len || isNotSecondCharacterInDirCode(move.charAt(i + 1))) {
            direction = "" + move.charAt(i);
        } else {
            direction = move.charAt(i) + "" + move.charAt(i + 1);
        }
        return direction;
    }


    private boolean isNotSecondCharacterInDirCode(char c) {
        return c != '>' && c != '=' && c != '<';
    }


    public MoveVector[] getAllMoveVectors() {
        return set.toArray(new MoveVector[0]);
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

//
//public PieceData(String moveVectorString) {
//    moveSet = new HashSet<>();
//    int[] vector = new int[2];
//    int pointer = 0;
//    int startOfNum = 0;
//    for (int i = 0; i < moveVectorString.length(); i++) {
//        if (moveVectorString.charAt(i) == ' ') {
//            moveSet.add(new MoveVector(vector));
//            pointer = 0;
//        } else if (moveVectorString.charAt(i) == ',') {
//            vector[pointer++] = Integer.parseInt(moveVectorString.substring(startOfNum, i));
//        } else {
//            continue;
//        }
//        startOfNum = i + 1;
//    }
//    moveSet.add(new MoveVector(vector));
//
//}
}
