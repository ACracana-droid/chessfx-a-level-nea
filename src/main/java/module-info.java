module com.chess.chessfx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.chess.chessfx to javafx.fxml;
    exports com.chess.chessfx;
    exports com.chess.chessfx.chessconfig;
    opens com.chess.chessfx.chessconfig to javafx.fxml;
    exports com.chess.chessfx.menu;
    opens com.chess.chessfx.menu to javafx.fxml;
}