package com.chessfx.ui.game;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.ChessGameModel;
import com.chessfx.engine.tiles.TileParent;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.TileColourPatternManager;
import com.chessfx.ui.game.tiles.TileUI;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.util.Builder;

public class ChessBoardBuilder implements Builder<StackPane> {

    protected ChessConfigModel chessConfig;
    protected StackPane container;

    protected BoardUIModel boardUIModel;
    protected ChessGameModel boardModel;

    public ChessBoardBuilder() {
        makeEmptyBoardUI();
    }

    private void makeEmptyBoardUI() {
        GridPane boardUI = new GridPane();
        container = new StackPane(boardUI);
        container.setStyle("-fx-border-color: green;");
        int numOfRanks = 8;
        int numOfFiles = 8;

        TileUI[][] boardUiArr = new TileUI[numOfRanks][numOfFiles];

        NumberBinding tileSize = Bindings.min(container.heightProperty().divide(numOfRanks * 1.15),
                container.widthProperty().divide(numOfFiles * 1.15));

        TileColourPatternManager tileColour = new TileColourPatternManager(1);

        for (int i = 0; i < numOfRanks; i++) {
            for (int j = 0; j < numOfFiles; j++) {
                boardUI.add(
                        boardUiArr[i][j] = new TileUI(
                                tileColour.getCycle(),
                                tileSize),
                        j, i
                );
            }
            tileColour.skipCycle();
        }
        boardUI.getStyleClass().add("chessboard");
    }

    public ChessBoardBuilder(ChessGameModel boardModel) {
        this.boardModel = boardModel;
        this.chessConfig = boardModel.getChessConfig();
        makeBoardUI(boardModel);
    }


    private void makeBoardUI(ChessGameModel representation) {
        GridPane boardUI = new GridPane();
        container = new StackPane(boardUI);

        TileParent[][] board = representation.getBoardAsTileArray2D();

        int numOfRanks = chessConfig.getHeight();
        int numOfFiles = chessConfig.getWidth();

        TileUI[][] boardUiArr = new TileUI[numOfRanks][numOfFiles];
        boardUIModel = new BoardUIModel(boardModel, boardUiArr);
        NumberBinding tileSize = Bindings.min(container.heightProperty().divide(numOfRanks * 1.15),
                container.widthProperty().divide(numOfFiles * 1.15));

        TileColourPatternManager tileColour = new TileColourPatternManager(chessConfig.getTileCheckerboardOffset());
        PieceSpriteFactory pieceSpriteFactory = new PieceSpriteFactory(chessConfig.getInstantiatedPiecesNameSet());

        for (int i = 0; i < numOfRanks; i++) {
            for (int j = 0; j < numOfFiles; j++) {
                boardUI.add(
                        boardUiArr[i][j] = new TileUI(board[i][j],
                                tileColour.getCycle(),
                                tileSize, pieceSpriteFactory,
                                boardUIModel, i, j),
                        j, i
                );
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
