module com.chess.chessfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires java.desktop;
    requires org.girod.javafx.svgimage;
    requires jdk.incubator.vector;
    requires java.management;

    opens com.chessfx.ui.menu to javafx.fxml;
    exports com.chessfx.chessconfig;
    exports com.chessfx.ui.menu;
    exports com.chessfx.ui.game;
    opens com.chessfx.ui.game to javafx.fxml;
    exports com.chessfx.engine.pieces;
    exports com.chessfx.engine.tiles;
    exports com.chessfx.ui.game.pieces;
    opens com.chessfx.ui.game.pieces to javafx.fxml;
    exports com.chessfx.ui.game.tiles;
    opens com.chessfx.ui.game.tiles to javafx.fxml;
    exports com.chessfx.engine.moves;
    exports com.chessfx.engine.board;
    exports com.chessfx.engine.bot;
    exports com.chessfx.ui.game.board;
    opens com.chessfx.ui.game.board to javafx.fxml;
    opens com.chessfx.chessconfig to javafx.fxml;
}