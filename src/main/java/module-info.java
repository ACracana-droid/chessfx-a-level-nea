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
    exports com.chess.chessfx.menu;
    opens com.chess.chessfx.menu to javafx.fxml;
    exports com.chess.chessfx.engine;
    opens com.chess.chessfx.engine to javafx.fxml;
    exports com.chess.chessfx.gameui;
    opens com.chess.chessfx.gameui to javafx.fxml;
    exports com.chess.chessfx.engine.pieces;
    opens com.chess.chessfx.engine.pieces to javafx.fxml;
    exports com.chess.chessfx.engine.tiles;
    opens com.chess.chessfx.engine.tiles to javafx.fxml;
    exports com.chess.chessfx.gameui.pieces;
    opens com.chess.chessfx.gameui.pieces to javafx.fxml;
    exports com.chess.chessfx.gameui.tiles;
    opens com.chess.chessfx.gameui.tiles to javafx.fxml;
}