package com.chessfx.ui.game.board;

import com.chessfx.engine.board.GameBoardContainer;
import com.chessfx.engine.board.BoardState;
import com.chessfx.engine.board.GridContainer;
import com.chessfx.engine.board.PlyData;
import com.chessfx.engine.bot.AttackMap;
import com.chessfx.engine.moves.Coordinate;
import com.chessfx.engine.moves.Move;
import com.chessfx.engine.pieces.Piece;
import com.chessfx.engine.tiles.TileBase;
import com.chessfx.log.Log;
import com.chessfx.ui.game.DragHandler;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.DevTileUI;
import com.chessfx.ui.game.tiles.TileUI;
import com.chessfx.ui.util.PopupManager;
import javafx.beans.property.DoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;

import java.util.ArrayList;
import java.util.List;

public class BoardUIModel implements GridContainer<TileUI> {

    // Selection
    private TileUI selectedTile; // there can only ever be 1.
    private final List<TileUI> prospectiveMoveTiles;
    private boolean reselectFlag = false;

    // Immutable:
    private final TileUI DEV_TILE;
    private final DragHandler dragHandler;
    private final DoubleProperty rotateDegreesProperty;
    private final PopupManager popupManager;
    private final GameBoardContainer game;
    private final PieceSpriteFactory pieceSpriteFactory;
    private final TileUI[][] boardUiTiles;


    // CONFIG
    private boolean isDragEnabled = true;

    public BoardUIModel(DragHandler dragHandler, GameBoardContainer game, TileUI[][] boardUiTiles,
                        PieceSpriteFactory pieceSpriteFactory, DoubleProperty rotateDegreesProperty,
                        PopupManager popupManager) {
        this.dragHandler = dragHandler;
        this.rotateDegreesProperty = rotateDegreesProperty;
        this.popupManager = popupManager;
        dragHandler.addEndDragHandler(this::endDragConsequence);
        this.game = game;

        this.pieceSpriteFactory = pieceSpriteFactory;
        this.boardUiTiles = boardUiTiles;

        this.prospectiveMoveTiles = new ArrayList<>();
        this.DEV_TILE = new DevTileUI();
        this.selectedTile = DEV_TILE;
    }


    private void select(TileUI tileUI, MouseEvent event) {
        selectedTile.setDefaultState();

        tileUI.setActive();
        selectedTile = tileUI;
        Log.debug("BoardUIModel", "Selected Piece");
        showMoves(getMovesToShow(tileUI));
        if (isDragEnabled) {
            dragHandler.startDrag(tileUI, event);
        }
    }

    public void endDragConsequence(MouseEvent event) {
        TileUI picked = dragHandler.endDrag(event);
        if (picked != null && prospectiveMoveTiles.contains(picked)) {
            /// do move

            doMove(selectedTile, picked);
            // reset board UI state.
            deselect();
            return;
        }
        if (reselectFlag) {
            reselectFlag = false;
            deselect();
        }
        if (picked != null && picked.equals(selectedTile)) {
            /// don't do anything

            return;
        }
        /// else, abort move
        deselect();


    }

    private List<Move> getMovesToShow(TileUI tileUI) {
        List<Move> unvalidatedMoves = tileUI.getTile().getPiece().moves(game);
        return game.getValidatedMoves(unvalidatedMoves);
    }

    private void showMoves(List<Move> moves) {
        Log.info("BoardUIModel", "Showing moves: ");
        prospectiveMoveTiles.clear();
        for (Move move : moves) {
            TileUI tileUI = getTile(move.destCoOrds());
            prospectiveMoveTiles.add(tileUI);
            tileUI.setProspectiveMove(move);
            Log.warn("Tile at " + move.getEndY() + " ," + move.getEndX() + " is valid move");
        }
    }

    private void deselect() {
        selectedTile.setDefaultState();
        for (TileUI tileUI : prospectiveMoveTiles) {
            tileUI.setDefaultState();
        }
        DEV_TILE.setActive();
        selectedTile = DEV_TILE;
    }

    /**
     * The central game interaction layer interacts with the board.
     * Performs various actions depending on the status of the tile clicked and the surrounding context.
     *
     * @param tileUI          the graphic pressed
     * @param tile            the backend tile
     * @param prospectiveMove the move proposed by the selected tile if there is one
     * @param event           the mouse event for the press
     */
    // TODO: Document new version
    public void handleTilePressed(TileUI tileUI, TileBase tile,
                                  Move prospectiveMove, MouseEvent event) {
        if (game.getGameModel().isGameOver()) {
            Log.info("Pressing tiles does not do anything, the game is over!");
            return;
        }
        Log.debug("Clicked tile: " + tileUI);
        Log.debug("Selected tile: " + selectedTile);
        if (prospectiveMove != null) { //// do move
            Log.debug("Prospective move: " + prospectiveMove);
            Log.info("Doing move...");
            doMove(selectedTile, tileUI);
            // reset board UI state.
            deselect();
            return;
        }

        if (selectedTile.equals(DEV_TILE)) {
            if (tile.hasPieceMatchingTeam(game.getCurrentTeam())) { //// initiate move.
                Log.info("Initiating move...");
                select(tileUI, event);
            }
        } else if (selectedTile.equals(tileUI)) {
            /// reselect (do basically nothing)...
            reselectFlag = true;
            select(selectedTile, event);
        } else { //// cancel move
            if (tile.hasPieceMatchingTeam(game.getCurrentTeam())) { //// cancel, select new
                Log.info("Cancelling move and selecting new piece");
                deselect();
                select(tileUI, event);
            } else { //// cancel
                Log.info("Cancelling move...");
                deselect();
            }
        }
    }

    /**
     * Upon correct user input, the game triggers UI updates and calls for the internal
     * data to be updated.
     *
     * @param from the TileUI which the move starts from.
     * @param to   the TileUI which was clicked.
     */
    private void doMove(TileUI from, TileUI to) {
        /// PRE-MOVE
        Move move = to.getProspectiveMove();

        Region sprite = from.removeExistingPieceSprite();
        to.removeExistingPieceSprite();

        // backend logic for move
        game.doMove(move);
        // (backend) move completed

        /// POST-MOVE
        to.showPieceSprite(sprite);
        // rotate board
        rotateDegreesProperty.set((rotateDegreesProperty.getValue() + 180) % 360);

        // (debug)
//        showAttackMap(boardContainer.getAttackMap());
        boardStateEffect(game.getGameModel().getBoardState());
    }

    private void boardStateEffect(BoardState boardState) {
        Coordinate kingCoOrds = game.getCurrentTeam().getKing().getCoOrds();
        TileUI tile = getTile(kingCoOrds);
        tile.showBoardStateEffect(boardState);

        // clear the effect from last round...
        Coordinate enemyKingCoOrds = game.getEnemyTeam().getKing().getCoOrds();
        TileUI otherTile = getTile(enemyKingCoOrds);
        otherTile.showBoardStateEffect(BoardState.DEFAULT);
    }


    public void showAttackMap(AttackMap attackMap) {
        for (int i = 0; i < game.HEIGHT; i++) {
            for (int j = 0; j < game.WIDTH; j++) {
                boardUiTiles[i][j].setThreatened(attackMap.getTile(i, j));
            }
        }
    }

    public void undoLastMoveIfPossible() {
        PlyData ply = game.undoLastMove();
        if (ply == null) {
            Log.warn("Undo move failed.");
            // TODO: Show that the undo failed in the UI state.

            return;
        }
        deselect();

        // Move back the last moved piece.
        Move move = ply.move();

        TileUI from = getTile(move.destCoOrds());
        TileUI to = getTile(move.startCoOrds());

        Region movedSprite = from.removePieceSprite();
        to.showPieceSprite(movedSprite);

        // reinstate captured pieces affected by this move.
        if (move.isSuperCapture()) {
            for (Piece p : move.getCaptures()) {
                getTile(p.getCoOrds()).showPieceSprite(pieceSpriteFactory.makePieceSprite(p));
            }
        }
        if (ply.directCapture() != null) {
            getTile(move.destCoOrds()).showPieceSprite(pieceSpriteFactory.makePieceSprite(ply.directCapture()));
        }

        // rotate board
        rotateDegreesProperty.set((rotateDegreesProperty.getValue() + 180) % 360);
    }


    public DoubleProperty rotateProperty() {
        return rotateDegreesProperty;
    }

    @Override
    public TileUI getTile(Coordinate coOrds) {
        return getTileFrom(boardUiTiles, coOrds);
    }

    @Override
    public TileUI[][] getBoard() {
        return boardUiTiles;
    }

    /**
     * Called when class is fully constructed.
     */
    public void gameStart() {
//        showAttackMap(boardContainer.getAttackMap());
    }


    public void promotionPrompt() {
        popupManager.promotionPopup(pieceSpriteFactory, game.getCurrentTeam());
    }
}
