package com.chessfx.ui.menu;

import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.log.Log;
import com.chessfx.ui.util.LayoutManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;


public abstract class LayoutBuilder implements Builder<Region> {

    protected final BooleanProperty layoutButtonDisabledProperty = new SimpleBooleanProperty(false);

    protected ChessConfigModel chessConfig;
    protected final LayoutManager layoutManager;
    protected final LayoutManager.Layout layout;

    public LayoutBuilder(ChessConfigModel chessConfig, LayoutManager layoutManager, LayoutManager.Layout layout) {
        this.chessConfig = chessConfig;
        this.layoutManager = layoutManager;
        this.layout = layout;
    }

    public void enableButtons() {
        layoutButtonDisabledProperty.set(false);
    }

    protected Button applyDisableSync(Button button) {
        button.disableProperty().bind(layoutButtonDisabledProperty);
        return button;
    }

    public EventHandler<ActionEvent> gameButtonAction(String chessConfigName, Runnable onCompleted) {
        return event -> {
            Task<Void> task = getChessConfigLoadTask(chessConfigName);
            task.setOnSucceeded(taskEvent -> {
                onCompleted.run();
                Log.debug("Button event completed successfully.");
            });
            Log.debug("Button pressed...");
            layoutButtonDisabledProperty.set(true);
            Thread thread = new Thread(task);
            thread.start();
        };
    }

    private Task<Void> getChessConfigLoadTask(String chessConfigName) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                Log.debug("... Button task started...");

                chessConfig.setChessConfigFromPropertiesResource(chessConfigName);
                Log.debug("... Button task final line...");
                return null;
            }
        };
    }

    public Button makeGameButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().addAll("game-button");
        button.setFocusTraversable(false);
        return button;
    }

    public Button makeGameButton(String text, Runnable onClicked) {
        Button button = makeGameButton(text);
        button.setOnMouseClicked(event -> onClicked.run());
        return button;
    }


    public EventHandler<ActionEvent> gameButtonAction(Runnable runnable) {
        return event -> {
            impactOfButtonAction();
            runnable.run();
        };
    }

    private void impactOfButtonAction() {
        layoutButtonDisabledProperty.set(true);
    }

    public Button returnButton(String text, Runnable goBack) {
        Button button = new Button(text);
        button.setOnAction(gameButtonAction(goBack));
        button.getStyleClass().add("return-button");
        return button;
    }


    public VBox returnButtonVbox(String text, Runnable goBack) {
        VBox vBox = new VBox(returnButton(text, goBack));
        vBox.getStyleClass().add("return-vbox");
        return vBox;
    }

    /**
     * Do not call if chess config has not been instantiated.
     *
     * @param chessConfigName
     * @return Button which starts the configured game when pressed
     */
    protected Button makeQuickGameButton(String chessConfigName) {
        Button button = new Button();
        button.setOnAction(gameButtonAction(chessConfigName, () -> layoutManager.startup(LayoutManager.Layout.GAME)));
        applyDisableSync(button);
        return button;
    }


    @Override
    abstract public Region build();

    public void startup() {
        enableButtons();
        layoutManager.setCurrentLayout(this.layout);
    }
}
