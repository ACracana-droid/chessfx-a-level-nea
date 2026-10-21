package com.chessfx.ui.util;

import com.chessfx.chessconfig.AppSettings;
import com.chessfx.chessconfig.ChessConfigModel;
import com.chessfx.log.Log;
import com.chessfx.ui.game.GameLayoutBuilder;
import com.chessfx.ui.menu.*;
import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import static com.chessfx.ui.util.LayoutManager.Layout.*;

/**
 * An alternative to my runnables.
 * A LayoutManager would give total freedom to all the layout builders it is distrubuted to.
 *
 */
public class LayoutManager {

    // Duration for the fade transition between layout switching
    private final Duration FADE_DURATION = Duration.seconds(0.4);

    // UI
    private final BorderPane appPane;

    private final PopupManager popupManager;

    // Backend
    private final ChessConfigModel chessConfig;
    private final AppSettings appSettings;

    private Layout currentLayout = MENU;

    public LayoutManager(BorderPane appLevelBorderPane, PopupManager popupManager, ChessConfigModel chessConfig, AppSettings appSettings) {
        Log.debug("LayoutManager", "Starting constructor...");
        this.appPane = appLevelBorderPane;
        this.popupManager = popupManager;
        this.chessConfig = chessConfig;
        this.appSettings = appSettings;
        initialiseLayouts();
        startup(currentLayout);
        Log.debug("LayoutManager", "Finished constructing.");
    }

    private void initialiseLayouts() {
        GAME.layoutBuilder = new GameLayoutBuilder(
                this, chessConfig, appSettings);

        MENU.layoutBuilder = new MenuLayoutBuilder(this, chessConfig);

        GAME_OPTIONS.layoutBuilder = new AllGameOptionsLayoutBuilder(this, chessConfig);

        SETTINGS.layoutBuilder = new SettingsLayoutBuilder(this, appSettings);

        for (Layout layout : Layout.values()) {
            LayoutBuilder builder = layout.layoutBuilder;
            layout.region = builder.build();
        }
    }

    public Region getLayout() {
        return currentLayout.region;
    }

    public Region getMenu() {
        return MENU.region;
    }

    /**
     *
     * Call this method to notify a {@code LayoutBuilder} that you are switching to it.
     * <p>
     * In this method, the LayoutBuilder must call {@code setCurrentLayout(this.layout)}.
     * For simplicity, this can be delegated to the superclass ({@code LayoutBuilder}) with
     * a call to the {@code super.startup()}.
     * </p>
     *
     * @param layout the layout being switched to
     */
    public void startup(Layout layout) {
        layout.layoutBuilder.startup();
    }

    /**
     * Call this method to switch to a new layout. This should usually be called from a {@code startup()} method.
     *
     * @param layout the layout being switched to
     */
    public void setCurrentLayout(Layout layout) {
        fadingLayoutChange(layout.region);
        currentLayout = layout;
    }

    public PopupManager getPopupManager() {
        return popupManager;
    }


    public enum Layout {
        MENU,
        GAME_OPTIONS,
        GAME,
        SETTINGS;

        private Region region;
        private LayoutBuilder layoutBuilder;

    }
//
//    public Runnable getStartupRunnable(Layout layout) {
//        if (layout.action == null) {
//            Log.error("LayoutManager", "STARTUP RUNNABLE FOR " + layout.name() + " IS NULL!");
//        }
//        return layout.action;
//    }

    private void fadeInLayout(Region layoutTo) {
        layoutTo.setOpacity(0.0);
        appPane.setCenter(layoutTo);
        FadeTransition fadeIn = new FadeTransition(FADE_DURATION, layoutTo);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
    }

    private void fadingLayoutChange(Region layoutTo) {
        Node layoutFrom = appPane.getCenter();
        FadeTransition fadeOut = new FadeTransition(FADE_DURATION, layoutFrom);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> fadeInLayout(layoutTo));
        fadeOut.play();
    }

    private void fadingLayoutChange(Region layout1, Region layout2) {
        FadeTransition fadeOut = new FadeTransition(FADE_DURATION, layout1);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {
            layout2.setOpacity(0.0);
            appPane.setCenter(layout2);
            FadeTransition fadeIn = new FadeTransition(FADE_DURATION, layout2);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
        });
        fadeOut.play();
    }
}
