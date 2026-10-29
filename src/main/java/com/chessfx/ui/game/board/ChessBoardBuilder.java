package com.chessfx.ui.game.board;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.board.GameBoardContainer;
import com.chessfx.engine.board.ReactiveChessContainer;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.ui.game.DragHandler;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.TileColourPatternManager;
import com.chessfx.ui.game.tiles.TileUI;
import com.chessfx.ui.util.LayoutManager;
import com.chessfx.ui.util.PopupManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.util.Builder;

public class ChessBoardBuilder implements Builder<StackPane> {

    protected ChessConfigModel chessConfig;

    protected final StackPane view = new StackPane();

    {
        view.getStyleClass().add("chessboard-wrapper");
    }

    protected final Pane dragLayer = new Pane();

    {
        dragLayer.setPickOnBounds(false);
        dragLayer.getStyleClass().add("drag-layer");
    }

    protected BoardUIModel boardUIModel;
    protected GameBoardContainer boardContainer;
    protected PopupManager popupManager;

    protected DoubleProperty rotateProperty = new SimpleDoubleProperty(0.0);



    public ChessBoardBuilder() {
        view.getChildren().addAll(makeEmptyBoardUI());
    }


    private Node makeEmptyBoardUI() {
        GridPane boardUI = makeGridPane();

        int numOfRanks = 8;
        int numOfFiles = 8;

        TileUI[][] boardUiArr = new TileUI[numOfRanks][numOfFiles];

        NumberBinding tileSize = getTileSize(numOfRanks, numOfFiles,
                view);

        applyGridConstraints(boardUI, tileSize, numOfRanks, numOfFiles);

        TileColourPatternManager tileColour = new TileColourPatternManager(1);

        for (int i = 0; i < numOfRanks; i++) {
            for (int j = 0; j < numOfFiles; j++) {
                boardUI.add(
                        boardUiArr[i][j] = new TileUI(
                                tileColour.getCycle()),
                        j, i
                );
            }
            tileColour.skipCycle();
        }
        return boardUI;
    }

    public ChessBoardBuilder(ReactiveChessContainer boardContainer, ChessConfigModel chessConfig, LayoutManager layoutManager) {
        this.boardContainer = boardContainer;
        this.chessConfig = chessConfig;
        this.popupManager = layoutManager.getPopupManager();
        Region board = makeBoardUI(boardContainer);
        board.rotateProperty().bind(rotateProperty);
        view.getChildren().addAll(board, dragLayer);
    }


    private Region makeBoardUI(ReactiveChessContainer representation) {
        GridPane boardUI = makeGridPane();


        TileBase[][] board = representation.getBoard();

        int numOfRanks = chessConfig.getHeight();
        int numOfFiles = chessConfig.getWidth();

        TileUI[][] boardUiArr = new TileUI[numOfRanks][numOfFiles];

        PieceSpriteFactory pieceSpriteFactory = new PieceSpriteFactory(chessConfig.getInstantiatedPiecesNameSet());
        DragHandler dragHandler = new DragHandler(dragLayer, boardUI, pieceSpriteFactory);
        boardUIModel = new BoardUIModel(dragHandler, boardContainer, boardUiArr, pieceSpriteFactory, rotateProperty, popupManager);

        NumberBinding tileSize = getTileSize(numOfRanks, numOfFiles, view);
        applyGridConstraints(boardUI, tileSize, numOfRanks, numOfFiles);

        initialiseBoard(numOfRanks, numOfFiles, boardUI, boardUiArr, board, pieceSpriteFactory, new TileColourPatternManager(chessConfig.getTileCheckerboardOffset()));
        return boardUI;
    }

    private void initialiseBoard(int numOfRanks, int numOfFiles, GridPane boardUI,
                                 TileUI[][] boardUiArr, TileBase[][] board,
                                 PieceSpriteFactory pieceSpriteFactory, TileColourPatternManager tileColour) {
        for (int i = 0; i < numOfRanks; i++) {
            for (int j = 0; j < numOfFiles; j++) {
                TileUI cur = new TileUI(board[i][j],
                        tileColour.getCycle(), pieceSpriteFactory,
                        boardUIModel, boardContainer.posString(new Coordinate(i, j)));

                // pieces always appear upright.
                cur.rotateProperty().bind(rotateProperty.negate());

                boardUiArr[i][j] = cur;

                boardUI.add(cur, j, i);
            }
            tileColour.skipCycle();
        }
    }

    private NumberBinding getTileSize(int numOfRanks, int numOfFiles,
                                      Pane container) {
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
        return view;
    }

    public BoardUIModel getBoardUIModel() {
        return boardUIModel;
    }
}
