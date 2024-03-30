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

    public static final float WIDTH = 20f;
    public static final float HEIGHT = 20f;
    public static final float SIZE = 1f; // size coefficient for almost all the object in the game
    private static final GameController INSTANCE = new GameController();
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
        return INSTANCE;
    }

    @Override
    public void create() {

        Box2D.init();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        viewport = new FitViewport(WIDTH, HEIGHT, camera);

        gameScreen = GameScreen.getInstance();
        menuScreen = MenuScreen.getInstance();

        currentModifiers = new HashSet<>();

        menuScreen.startNewGame();
        setScreen(menuScreen);

    }

//    public void startNewGame() {
//        gameScreen.finishLevel();
//        currentModifiers.clear();
//        menuScreen.startNewGame();
//        setScreen(menuScreen);
//    }

    public void startNewLevel() {
        gameScreen.startLevel();
        setScreen(gameScreen);
    }

    @Override
    public void setScreen(Screen screen) {
        super.setScreen(screen);
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
    }

    public void addModifier(Modifier newModifier) {

        currentModifiers.add(newModifier);

        // Set doesn't have get method, so we use for-each
        for (Modifier modifier : currentModifiers) {
            if (modifier.equals(newModifier)) {
                modifier.increaseAmount();
                break;
            }
        }

    }

    public void finishLevel() {
        setScreen(menuScreen);
    }

    public void death() {
        //startNewGame();
    }

    @Override
    public void render() {
        super.render(); // important!

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            gameScreen.pause();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }

    }

    @Override
    public void dispose() {
        gameScreen.dispose();
        menuScreen.dispose();
    }

}
