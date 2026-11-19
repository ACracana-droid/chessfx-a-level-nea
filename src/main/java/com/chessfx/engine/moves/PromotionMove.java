package com.chessfx.engine.moves;

import com.chessfx.engine.pieces.Piece;

public class PromotionMove extends Move {

    private final String promotionName;

    public PromotionMove(Piece piece, String promotionName, int y, int x, boolean isCapture) {
        super(piece, y, x, isCapture);
        this.promotionName = promotionName;
    }

    public PromotionMove(Move prospectiveMove, String pieceName) {
        super(prospectiveMove);
        promotionName = pieceName;
    }

    public String getPromotionPieceName() {
        return promotionName;
    }
}
