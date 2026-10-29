package com.chessfx.engine.board;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.engine.moves.Move;
import com.chessfx.log.Log;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MoveValidationTest {

    @Test
    void scholarsMate() {
        Log.setThreshold(10000);
        GameBoardContainer game = makeDefaultGame();
        Log.setThreshold(0);

        game.playMoves(List.of("e2-e4", "h7-h6", "d1-h5", "a7-a6", "f1-c4", "b7-b6"));
        // Mate (Scholar's Mate)
        game.doMoveWithValidation("h5xf7");

        BoardState state = game.getBoardState();
        Log.debug("STATE IS: " + state.getText());
        assertTrue(state == BoardState.CHECKMATE);

    }

    private GameBoardContainer makeDefaultGame() {
        // Setup
        ChessConfigModel chessConfig = new ChessConfigModel(false);

        chessConfig.setChessConfigFromPropertiesResource("defaultPvP.properties");
        GameStateModel gameStateModel = new GameStateModel(chessConfig);
        GameBoardContainer game = new GameBoardContainer(gameStateModel, chessConfig.getGameBoard());
        game.gameStart();
        return game;
    }

    @Test
    void checkWithValidMovesToCapturePieceThreateningCheck() {
        Log.setThreshold(Integer.MAX_VALUE);
        GameBoardContainer game = makeDefaultGame();
        game.playMoves(List.of("c2-c3", "h7-h6", "d1-a4", "h6-h5", "a4xd7"));
        // check...
        Log.setThreshold(-100);

        List<Move> queenMoves = game.getValidatedMoves(game.getTile(game.pos("d8")).getPiece().moves(game));
        Log.warn("Queen moves", queenMoves.toString());
//        assertFalse(game.attackMap.getTile(game.pos("e8")));
        Log.warn("King moves", game.getValidatedMoves(game.getTile(game.pos("e8")).getPiece().moves(game)).toString());


        assertFalse(queenMoves.isEmpty(), "Queen should have move to capture the enemy threat.");
        assertFalse(game.getValidatedMoves(game.getTile(game.pos("e8")).getPiece().moves(game)).isEmpty(), "King should have move to capture enemy threat.");
        assertTrue(game.getValidatedMoves(game.getTile(game.pos("a8")).getPiece().moves(game)).isEmpty(), "Rook should have no valid moves due to check.");
    }
}
