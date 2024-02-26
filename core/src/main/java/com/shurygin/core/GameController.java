package com.shurygin.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shurygin.core.modifiers.Modifier;
import com.shurygin.core.screens.GameScreen;
import com.shurygin.core.screens.MenuScreen;

import java.util.HashSet;
import java.util.Set;


public class GameController extends Game {

    private static GameController instance;
    public static boolean debug;
    private GameScreen gameScreen;
    private MenuScreen menuScreen;
    private OrthographicCamera camera;
    private Viewport viewport;

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private Set<Modifier> currentModifiers;

    public Set<Modifier> getCurrentModifiers() {
        return currentModifiers;
    }

    private GameController() {
    }

    public static GameController getInstance() {
        if (instance == null) instance = new GameController();
        return instance;
    }

    public void create() {

        Box2D.init();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WIDTH, Constants.HEIGHT);
        viewport = new FitViewport(Constants.WIDTH, Constants.HEIGHT, camera);

        gameScreen = GameScreen.getInstance();
        menuScreen = MenuScreen.getInstance();

        currentModifiers = new HashSet<>();

        menuScreen.startNewGame();
        changeScreen(menuScreen);

    }

    public void startNewGame() {
        gameScreen.finishLevel();
        currentModifiers.clear();
        menuScreen.startNewGame();
        changeScreen(menuScreen);
    }

    public void startNewLevel() {
        gameScreen.startLevel();
        changeScreen(gameScreen);
    }

    private void changeScreen(Screen screen) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        this.setScreen(screen);
    }

    public void addModifier(Modifier newModifier) {

        currentModifiers.add(newModifier);

        // Set doesn't have get method, so we use for-each (no needs optimization here)
        for (Modifier modifier : currentModifiers) {
            if (modifier.equals(newModifier)) {
                modifier.increaseAmount();
                break;
            }
        }

    }

    public void finishLevel() {
        changeScreen(menuScreen);
    }

    public void death() {
        startNewGame();
    }

    public void render() {
        super.render(); // important!

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            if (gameScreen.isPaused()) {
                gameScreen.cancelPause();
            } else {
                gameScreen.pause();
            }

        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }

    }

    public void dispose() {
        gameScreen.dispose();
        menuScreen.dispose();
    }

}
