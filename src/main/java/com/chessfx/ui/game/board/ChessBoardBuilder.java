package com.chessfx.ui.game.board;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.board.BoardContainer;
import com.chessfx.engine.board.ReactiveChessContainer;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.TileColourPatternManager;
import com.chessfx.ui.game.tiles.TileUI;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class ChessBoardBuilder implements Builder<StackPane> {

    protected ChessConfigModel chessConfig;
    protected StackPane container;

    protected BoardUIModel boardUIModel;
    protected BoardContainer boardContainer;

    public ChessBoardBuilder() {
        makeEmptyBoardUI();
    }

    private void makeEmptyBoardUI() {
        GridPane boardUI;
        container = new StackPane(boardUI = makeGridPane());
        container.getStyleClass().add("chessboard-wrapper");
        // TODO: set style class.
        int numOfRanks = 8;
        int numOfFiles = 8;

        TileUI[][] boardUiArr = new TileUI[numOfRanks][numOfFiles];

        NumberBinding tileSize = getTileSize(numOfRanks, numOfFiles);

        applyGridConstraints(boardUI, tileSize, numOfRanks, numOfFiles);

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
    }

    public ChessBoardBuilder(ReactiveChessContainer chessContainer, ChessConfigModel chessConfig) {
        this.boardContainer = chessContainer;
        this.chessConfig = chessConfig;
        makeBoardUI(chessContainer);
    }


    private void makeBoardUI(ReactiveChessContainer representation) {
        GridPane boardUI = makeGridPane();
        container = new StackPane(boardUI);
        container.getStyleClass().add("chessboard-wrapper");

        TileBase[][] board = representation.getBoard();

        int numOfRanks = chessConfig.getHeight();
        int numOfFiles = chessConfig.getWidth();

        TileUI[][] boardUiArr = new TileUI[numOfRanks][numOfFiles];

        PieceSpriteFactory pieceSpriteFactory = new PieceSpriteFactory(chessConfig.getInstantiatedPiecesNameSet());

        boardUIModel = new BoardUIModel(boardContainer, boardUiArr, pieceSpriteFactory);

        NumberBinding tileSize = getTileSize(numOfRanks, numOfFiles);
        applyGridConstraints(boardUI, tileSize, numOfRanks, numOfFiles);

        initialiseBoard(numOfRanks, numOfFiles, boardUI, boardUiArr, board, tileSize, pieceSpriteFactory, new TileColourPatternManager(chessConfig.getTileCheckerboardOffset()));
    }

    private void initialiseBoard(int numOfRanks, int numOfFiles, GridPane boardUI,
                                 TileUI[][] boardUiArr, TileBase[][] board, NumberBinding tileSize,
                                 PieceSpriteFactory pieceSpriteFactory, TileColourPatternManager tileColour) {
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
    }

    private NumberBinding getTileSize(int numOfRanks, int numOfFiles) {
        final double SCALE_FACTOR = 1.06;
        NumberBinding tileSize = Bindings.min(container.heightProperty().divide(numOfRanks * SCALE_FACTOR),
                container.widthProperty().divide(numOfFiles * SCALE_FACTOR));
        return tileSize;
    }

    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();
        gridPane.getStyleClass().add("chessboard");

        return gridPane;

    }

    private void applyGridConstraints(GridPane boardUI, NumberBinding tileSize, int numOfRanks, int numOfFiles) {
        RowConstraints rankConstraint =
                getRankConstraints(tileSize);
        ColumnConstraints fileConstraint =
                getFileConstraints(tileSize);

        for (int i = 0; i < numOfRanks; i++) {
            boardUI.getRowConstraints().add(rankConstraint);
        }
        for (int i = 0; i < numOfFiles; i++) {
            boardUI.getColumnConstraints().add(fileConstraint);
        }

    }

    private ColumnConstraints getFileConstraints(NumberBinding tileSize) {
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setMinWidth(30);
        constraints.prefWidthProperty().bind(tileSize);
        return constraints;
    }

    private RowConstraints getRankConstraints(NumberBinding tileSize) {
        RowConstraints constraints = new RowConstraints();
        constraints.setMinHeight(30);
        constraints.prefHeightProperty().bind(tileSize);
        return constraints;
    }

    @Override
    public StackPane build() {
        return container;
    }

    public BoardUIModel getBoardUIModel() {
        return boardUIModel;
    }
}
