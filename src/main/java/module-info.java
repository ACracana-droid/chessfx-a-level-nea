module com.chess.chessfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires org.girod.javafx.svgimage;

    opens com.chess.chessfx to javafx.fxml;
    exports com.chess.chessfx;
    exports com.chess.chessfx.chessconfig;
    opens com.chess.chessfx.chessconfig to javafx.fxml;
    exports com.chess.chessfx.ui.menu;
    opens com.chess.chessfx.ui.menu to javafx.fxml;
    exports com.chess.chessfx.engine;
    opens com.chess.chessfx.engine to javafx.fxml;
    exports com.chess.chessfx.ui.game;
    opens com.chess.chessfx.ui.game to javafx.fxml;
    exports com.chess.chessfx.engine.pieces;
    opens com.chess.chessfx.engine.pieces to javafx.fxml;
    exports com.chess.chessfx.engine.tiles;
    opens com.chess.chessfx.engine.tiles to javafx.fxml;
    exports com.chess.chessfx.ui.game.pieces;
    opens com.chess.chessfx.ui.game.pieces to javafx.fxml;
    exports com.chess.chessfx.ui.game.tiles;
    opens com.chess.chessfx.ui.game.tiles to javafx.fxml;
}