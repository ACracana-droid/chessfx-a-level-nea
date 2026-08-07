package com.chess.chessfx.gameui;

import com.chess.chessfx.chessconfig.ChessConfigModel;
import com.chess.chessfx.engine.BoardRepresentation;
import com.chess.chessfx.engine.tiles.TileParent;
import com.chess.chessfx.gameui.pieces.PieceSpriteFactory;
import com.chess.chessfx.gameui.tiles.TileColourPatternManager;
import com.chess.chessfx.gameui.tiles.TileUI;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

public class ChessBoardBuilder implements Builder<StackPane> {

    ChessConfigModel chessConfig;
    StackPane container;
    BoardRepresentation representation;

    public ChessBoardBuilder(BoardRepresentation representation) {
        this.representation = representation;
        this.chessConfig = representation.getChessConfig();

        makeBoardUI();
    }

    private void makeBoardUI() {
        GridPane boardUI = new GridPane();
        container = new StackPane(boardUI);

        // This has no real impact, all that matters are the sizes of the tiles inside boardUI.
//        boardUI.prefHeightProperty().bind(container.heightProperty());
//        boardUI.prefWidthProperty().bind(container.widthProperty());

        TileParent[][] board = representation.getBoardAsTileArray2D();

        int numOfRanks = chessConfig.getHeight();
        int numOfFiles = chessConfig.getWidth();
        NumberBinding tileSize = Bindings.min(container.heightProperty().divide(numOfRanks*1.15),
                container.widthProperty().divide(numOfFiles * 1.15));

        TileColourPatternManager tileColour = new TileColourPatternManager(chessConfig.getTileCheckerboardOffset());
        PieceSpriteFactory pieceSpriteFactory = new PieceSpriteFactory(chessConfig.getInstantiatedPiecesIdSet());

        for (int i = 0; i < numOfRanks; i++) {
            for (int j = 0; j < numOfFiles; j++) {
                boardUI.add(new TileUI(board[i][j],
                        tileColour.getCycle(), tileSize, pieceSpriteFactory), j, i);
            }
            tileColour.skipCycle();
        }

        boardUI.getStyleClass().add("chessboard");
    }

    @Override
    public StackPane build() {
        return container;
    }
}
