package com.chess.chessfx.gameui;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import com.chess.chessfx.engine.BoardRepresentation;
import com.chess.chessfx.engine.Tile;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ChessBoard extends Node {
    ChessConfigModel chessConfig;

    public ChessBoard(BoardRepresentation representation) {
        chessConfig = representation.getChessConfig();
        buildBoardGUI(representation.getBoardAsTileArray2D());
    }

    private void buildBoardGUI(Tile[][] board) {
        VBox vBox = new VBox(makeRankHboxes(board));
    }

    private HBox[] makeRankHboxes(Tile[][] board) {
        // TODO: don't use these actually.
        HBox[] hBoxes = new HBox[chessConfig.getHeight()];

        for (int rank = 0; rank < chessConfig.getHeight(); rank++) {
            hBoxes[rank] = new HBox(createRankUI(board[rank], TileColour.LIGHT));
            hBoxes[rank].getStyleClass().add("board-rank");

        }
        return hBoxes;
    }

    private TileUI[] createRankUI(Tile[] rank, TileColour tileColour) {
        TileUI[] rankUI = new TileUI[chessConfig.getWidth()];
        for (int file = 0; file < chessConfig.getWidth(); file++) {
            rankUI[file] = new TileUI(rank[file], tileColour.cycle());
        }
        return null;
    }
}
