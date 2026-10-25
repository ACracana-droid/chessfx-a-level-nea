package com.chessfx.ui.game;

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
    private double dragY, dragX;

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
        dragX = -(width / 2);
        dragY = -(height / 2);


        // Updating global variable state...
        draggedPiece = pieceSprite;
        initialDrag = true;

        // Need to swap sprites so JavaFX knows which piece sprite was pressed...
        Region ghostSprite = pieceSpriteFactory.makePieceSprite(selectedTile.getTile());
        draggedPiece = selectedTile.swapSprites(ghostSprite);

        ghostPiece = ghostSprite;

        dragPane.getChildren().add(draggedPiece);
        draggedPiece.setVisible(false);
        draggedPiece.setMouseTransparent(true);
        draggedPiece.setManaged(false);

//
//        Point2D originalPieceCoOrds = boardGrid.localToScene(tile.getLayoutX(), tile.getLayoutY());
//
//        Point2D destinationPieceCoOrds = dragPane.sceneToLocal(originalPieceCoOrds);
//        pieceSprite.setLayoutX(destinationPieceCoOrds.getX());
//        pieceSprite.setLayoutY(destinationPieceCoOrds.getY());


    }

    /**
     * Traverses the scene graph recursively, like one would a tree.
     * This enables me to find a child node that has been clicked
     * by checking down from the parent node
     *
     * @param sceneX the x-coordinate relative to the scene
     * @param sceneY the y-coordinate relative to the scene
     * @return the tile that was clicked, null if no such tile exists
     */
    //TODO: Write pseudocode in complex code.
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

    private TileUI matchTileCoOrdinates(Node root, double sceneX, double sceneY) {
        if (root instanceof TileUI tile) {
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
            ghostPiece.getStyleClass().add("ghost");

            initialDrag = false;
            draggedPiece.setVisible(true);
        }

        hoverPseudoclassWorkAround(event.getSceneX(), event.getSceneY());

        Point2D paneCoOrd = dragPane.sceneToLocal(event.getSceneX(), event.getSceneY());
        double maxX = dragPane.getWidth() - draggedPiece.getWidth();
        double maxY = dragPane.getHeight() - draggedPiece.getHeight();

        // prevent going outside the dragPane.
        double x = Math.clamp(paneCoOrd.getX() + dragX, 0, maxX);
        double y = Math.clamp(paneCoOrd.getY() + dragY, 0, maxY);


        // Keep it centered on the cursor as it moves
        draggedPiece.setLayoutX(x);
        draggedPiece.setLayoutY(y);
    }


    public TileUI endDrag(MouseEvent event) {
        Region piece = draggedPiece;
        draggedPiece = null;

        // if mouse is hovering over tile which is prospective move, highlight especially.
        // if mouse is hovering over tile with prospective move and ends drag, make move.
        dragPane.getChildren().remove(piece);

        // remove ghost state
        selectedTile.getPieceSprite().getStyleClass().removeLast();


        double x = event.getSceneX(), y = event.getSceneY();
        TileUI tile = findTile( x, y);
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
        if (quasiHovered != null) {
            quasiHovered.setQuasiHover(false);
        }
        quasiHovered = findTile(x, y);
        if (quasiHovered != null) {
            quasiHovered.setQuasiHover(true);
        }
    }


    public void addEndDragHandler(EventHandler<MouseEvent> dragEnd) {
        dragPane.setOnMouseReleased(dragEnd);
    }
}
