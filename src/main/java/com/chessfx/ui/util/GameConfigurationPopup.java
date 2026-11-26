package com.chessfx.ui.util;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.log.Log;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

import java.util.ArrayList;
import java.util.List;

public class GameConfigurationPopup extends BorderPane {

    private final boolean isVsBot;

    private final List<String> tooltips = new ArrayList<>(List.of("The normal rules for chess (FIDE)", "TQOO", "TODO rules for chess (FIDE)"));
    private final List<String> gameNames = new ArrayList<>(List.of("Standard", "Capablanca", "Chess960"));
    private final List<String> fileNames = new ArrayList<>(List.of("standard", "capablanca", "chess960"));



    private Button playButton;
    private final LayoutManager layoutManager;
    private final ChessConfigModel chessConfig;
    private final PopupManager popupManager;

    public GameConfigurationPopup(boolean isVsBot, LayoutManager layoutManager, ChessConfigModel chessConfig) {
        super();

        this.layoutManager = layoutManager;
        popupManager = layoutManager.getPopupManager();
        this.chessConfig = chessConfig;
        this.getStyleClass().add("popup");
//        this.setTop(topBar(closeButton));
        this.setCenter(gameChooser());
        this.isVsBot = isVsBot;
        playButton.requestFocus();
    }

    private VBox gameChooser() {
        SearchableComboBox<String> searchableComboBox = makeComboBox();
        ReadOnlyIntegerProperty selectedIndexProperty = searchableComboBox.getSelectionModel().selectedIndexProperty();
        VBox vBox = new VBox(
                // Title:
                topBar(),

                // Game Type:
                labelPair("Game Type:", searchableComboBox),
                gameTypeDescription(selectedIndexProperty),
                // Timer
                labelPair("Timer:", new Label("not yet done")),
                // Play
                playButton = playButton(selectedIndexProperty));
        vBox.getStyleClass().add("game-config-vbox");
        return vBox;
    }

    private Label gameTypeDescription(ReadOnlyIntegerProperty gameTypeIdx) {
        Label label = new Label(tooltips.getFirst());
        gameTypeIdx.addListener(
                (observable, oldValue, newValue) -> {
                    int idx = newValue.intValue();
                    if (idx < 0) return;
                    label.setText(tooltips.get(newValue.intValue()));
                });
        return label;
    }

    private HBox labelPair(String s, Node node) {
        HBox hBox = new HBox(label(s), node);
        hBox.getStyleClass().addAll("config-hbox");
        return hBox;
    }

    private Label label(String s) {
        Label label = new Label(s);
        return label;
    }


    private Button playButton(ReadOnlyIntegerProperty selectedIndexProperty) {
        Button button = new Button("Play");
        button.setOnAction(event -> {
            gameButtonAction(selectedIndexProperty);
            popupManager.pop();
            popupManager.toggleLock();
        });

        button.getStyleClass().add("game-button");
        return button;
    }


    public void gameButtonAction(ReadOnlyIntegerProperty selectedIndexProperty) {
        Task<Void> task = getChessConfigLoadTask(fileNames.get(selectedIndexProperty.get()));
        task.setOnSucceeded(taskEvent -> {
            layoutManager.startup(LayoutManager.Layout.GAME);
            Log.debug("Button event completed successfully.");
        });
        Log.debug("Button pressed...");
        Thread thread = new Thread(task);
        thread.start();
    }

    private Task<Void> getChessConfigLoadTask(String chessConfigName) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                Log.debug("... Button task started...");
                chessConfig.setChessConfigFromPropertiesResource(chessConfigName + ".properties");
                chessConfig.setIsVsBot(isVsBot);
                Log.debug("... Button task final line...");
                return null;
            }
        };
    }


    private SearchableComboBox<String> makeComboBox() {
        SearchableComboBox<String> searchableComboBox = new SearchableComboBox<>(FXCollections.observableArrayList(gameTypesList()));
        searchableComboBox.getSelectionModel().selectFirst();
        return searchableComboBox;
    }

    private List<String> gameTypesList() {
        return gameNames;
    }

    private HBox topBar() {
        Button button = popupManager.closeButton();

        HBox hBox = new HBox(button, title());
        hBox.getStyleClass().add("game-top-hbox");
        return hBox;
    }

    private Node title() {
        Label label = new Label("Game Configuration");
        return label;
    }

}
