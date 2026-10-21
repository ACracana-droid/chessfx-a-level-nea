package com.chessfx.ui.game;

import com.chessfx.log.Log;
import com.chessfx.ui.game.pieces.PieceSpriteFactory;
import com.chessfx.ui.game.tiles.TileUI;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
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
    private TileUI selectedTile;

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

        Region ghostSprite = pieceSpriteFactory.makePieceSprite(tile.getTile());
        ghostSprite.getStyleClass().add("ghost");
        // Fixing size so it does not change whilst moving tiles.
        double width = pieceSprite.getWidth(), height = pieceSprite.getHeight();
        pieceSprite.setPrefSize(width, height);
        // Record the offset to get to the centre of the piece sprite
        dragX = -(width / 2);
        dragY = -(height / 2);
        // Need to swap sprites so JavaFX knows which piece sprite was pressed...
        pieceSprite = tile.swapSprites(ghostSprite);
        // Updating global variable state...
        draggedPiece = pieceSprite;

        Log.debug("Swapped piece from tile...");
        dragPane.getChildren().add(pieceSprite);
        pieceSprite.setMouseTransparent(true);
        pieceSprite.setManaged(false);

        Point2D originalPieceCoOrds = boardGrid.localToScene(tile.getLayoutX(), tile.getLayoutY());

        Point2D destinationPieceCoOrds = dragPane.sceneToLocal(originalPieceCoOrds);
        pieceSprite.setLayoutX(destinationPieceCoOrds.getX());
        pieceSprite.setLayoutY(destinationPieceCoOrds.getY());


        pieceSprite.toFront();
    }

    private TileUI findTile(Node root, double sceneX, double sceneY) {
        if (root instanceof TileUI tile) {
            // Check if the scene coordinates fall within this tile's local space
            Point2D localCoord = tile.sceneToLocal(sceneX, sceneY, true);
            if (tile.contains(localCoord) && tile.isVisible()) {
                return tile;
            }
            return null;
        }

        if (root instanceof Parent) {
            Parent parent = (Parent) root;
            // Iterate children in reverse order (top-most z-order first)
            ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (int i = children.size() - 1; i >= 0; i--) {
                Node child = children.get(i);
                if (child.isMouseTransparent()) continue;

                TileUI found = findTile(child, sceneX, sceneY);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }


    public void dragContinue(MouseEvent event) {
        if (draggedPiece == null) return;
        Log.debug("Dragging continues...");

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
        Log.debug("Dragging over.");
        Region piece = draggedPiece;
        draggedPiece = null;

        // if mouse is hovering over tile which is prospective move, highlight especially.
        // if mouse is hovering over tile with prospective move and ends drag, make move.
        dragPane.getChildren().remove(piece);

        // remove ghost state
        selectedTile.getPieceSprite().getStyleClass().removeLast();


        // Check 2
        Log.warn(event.getTarget().toString());
        double x = event.getSceneX(), y = event.getSceneY();
        TileUI tile = findTile(boardGrid, x, y);
        return tile;
    }


    public void addEndDragHandler(EventHandler<MouseEvent> dragEnd) {
        dragPane.setOnMouseReleased(dragEnd);
    }
}
