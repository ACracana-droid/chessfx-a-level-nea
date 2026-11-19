package com.chessfx.ui.game.board;

import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.TileUI;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

public class DragHandler {

    private final Pane dragPane;
    private final GridPane boardGrid;
    private final PieceSpriteFactory pieceSpriteFactory;


    // DRAG STATE:
    private double dragOffsetY, dragOffsetX;

    private Region draggedPiece;
    private Region ghostPiece;

    private TileUI selectedTile;

    private TileUI quasiHovered = null;
    private boolean initialDrag = true;


    public DragHandler(Pane dragPane, GridPane boardGrid, PieceSpriteFactory pieceSpriteFactory) {
        this.dragPane = dragPane;
        this.boardGrid = boardGrid;
        this.pieceSpriteFactory = pieceSpriteFactory;

        EventHandler<MouseEvent> mouseDragHandler = this::dragContinue;
        dragPane.setOnMouseDragged(mouseDragHandler);
    }

    public void startDrag(TileUI tile, MouseEvent event) {
        this.selectedTile = tile;
        Region pieceSprite = tile.getPieceSprite();

        // Fixing size so it does not change whilst moving tiles.
        double width = pieceSprite.getWidth(), height = pieceSprite.getHeight();
        pieceSprite.setPrefSize(width, height);
        // Record the offset to get to the centre of the piece sprite
        dragOffsetX = -(width / 2);
        dragOffsetY = -(height / 2);

        // Updating global variable state...
        draggedPiece = pieceSprite;
        initialDrag = true;

        // Swap sprites so JavaFX knows which piece sprite was pressed
        // JavaFX natively only drags what was initially pressed
        Region ghostSprite = pieceSpriteFactory.makePieceSprite(selectedTile.getTile());
        draggedPiece = selectedTile.swapSprites(ghostSprite);

        ghostPiece = ghostSprite;

        dragPane.getChildren().add(draggedPiece);
        // Hide draggedPiece from view before dragging starts.
        // ghostSprite is still visible with no changes.
        draggedPiece.setVisible(false);
        draggedPiece.setMouseTransparent(true);
        draggedPiece.setManaged(false);
    }

    /**
     * Traverses the scene graph down from the parent GridPane to the child tileUI
     * This enables me to find a child tileUI that has experienced a mouse event.
     *
     * @param sceneX the x-coordinate relative to the scene
     * @param sceneY the y-coordinate relative to the scene
     * @return the tile that was clicked, null if no such tile exists
     */
    //TODO: Consider writing pseudocode
    private TileUI findTile(double sceneX, double sceneY) {
        ObservableList<Node> children = boardGrid.getChildrenUnmodifiable();
        for (int i = 0; i < children.size(); i++) {
            Node child = children.get(i);
            if (child.isMouseTransparent()) continue;

            TileUI found = matchTileCoOrdinates(child, sceneX, sceneY);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    private TileUI matchTileCoOrdinates(Node possibleTile, double sceneX, double sceneY) {
        if (possibleTile instanceof TileUI tile) {
            Point2D localCoord = tile.sceneToLocal(sceneX, sceneY, true);
            if (tile.contains(localCoord) && tile.isVisible()) {
                return tile;
            }
            return null;
        }
        return null;
    }


    private void dragContinue(MouseEvent event) {
        if (draggedPiece == null) return;
        if (initialDrag) {
            // make piece sprite still on tile transparent
            ghostPiece.getStyleClass().add("ghost");

            initialDrag = false;
            // show the piece being dragged.
            draggedPiece.setVisible(true);
        }

        // add hover pseudoclass states to tiles that are hovered over
        hoverPseudoclassWorkAround(event.getSceneX(), event.getSceneY());

        // get coordinates relative to the dragPane node: top left of node is (0,0).
        Point2D paneCoOrd = dragPane.sceneToLocal(event.getSceneX(), event.getSceneY());
        // get maximum values of x and y for a draggedPiece node to be inside the dragpane.
        double maxX = dragPane.getWidth() - draggedPiece.getWidth();
        double maxY = dragPane.getHeight() - draggedPiece.getHeight();

        // prevent going outside the dragPane.
        double x = Math.clamp(paneCoOrd.getX() + dragOffsetX, 0, maxX);
        double y = Math.clamp(paneCoOrd.getY() + dragOffsetY, 0, maxY);

        // move piece to centre of courser
        draggedPiece.setLayoutX(x);
        draggedPiece.setLayoutY(y);
    }


    public TileUI endDrag(MouseEvent event) {
        if (draggedPiece == null) return null;
        Region piece = draggedPiece;
        draggedPiece = null;

        // if mouse is hovering over tile which is prospective move, highlight especially.
        // if mouse is hovering over tile with prospective move and ends drag, make move.
        dragPane.getChildren().remove(piece);

        // remove ghost state (transparency)
        selectedTile.getPieceSprite().getStyleClass().removeLast();


        double x = event.getSceneX(), y = event.getSceneY();
        // get the tile mouse was hovering over (if exists)
        TileUI tile = findTile(x, y);
        return tile;
    }

    /**
     * The hover pseudoclass state is not normally active when the mouse is pressed down.
     * This work-around ensures that it is.
     *
     * @param x x-coordinate relative to scene
     * @param y y-coordinate relative to scene
     */
    private void hoverPseudoclassWorkAround(double x, double y) {
        TileUI newQuasiHovered = findTile(x,y);
        // object references should be equal, hence not using .equals()
        if (newQuasiHovered == quasiHovered) return;

        // remove pseudoclass on previous tile
        if (quasiHovered != null) {
            quasiHovered.setQuasiHover(false);
        }

        // assign pseudoclass to new tile.
        quasiHovered = newQuasiHovered;
        if (quasiHovered != null) {
            quasiHovered.setQuasiHover(true);
        }
    }


    public void addEndDragHandler(EventHandler<MouseEvent> dragEnd) {
        dragPane.setOnMouseReleased(dragEnd);
    }
}
