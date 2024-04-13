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
import com.shurygin.core.menu.MenuController;
import com.shurygin.core.modifiers.Modifier;
import com.shurygin.core.utils.PauseObservable;

import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

public class GameController extends Game {

    public static final float WIDTH = 20f;
    public static final float HEIGHT = 20f;
    public static final float SIZE = 1f; // size coefficient for almost all objects in the game

    public static boolean debug;

    private final OrthographicCamera camera = new OrthographicCamera();
    private Viewport viewport = new FitViewport(WIDTH, HEIGHT, camera);;

    private MenuController menuController;
    private LevelController levelController;
    private PauseObservable pauseObservable = new PauseObservable();
    private boolean pause;

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Viewport getViewport() {
        return viewport;
    }

    private final Set<Modifier> modifiers = new HashSet<>(); // Current modifiers

    @Override
    public void create() {

        menuController = new MenuController(this);
        levelController = new LevelController(this);

        Box2D.init();
        camera.setToOrtho(false, WIDTH, HEIGHT);
        startNewGame();
    }

    public void startNewGame(){
        modifiers.clear();
        menuController.showMenu();
    }

    public void startNewLevel() {
        levelController.startNewLevel();
    }

    public static void changeScreen(Screen screen) {
        Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        getInstance().setScreen(screen);
    }

    public void addModifier(Modifier newModifier) {

        modifiers.add(newModifier);

        // Set doesn't have GET method, so we use for-each
        for (Modifier modifier : modifiers) {
            if (modifier.equals(newModifier)) {
                modifier.increaseAmount();
                break;
            }
        }

    }

    public void completeLevel() {
        menuController.showMenu();
    }

    public void death() {
        //startNewGame();
    }

    @Override
    public void render() {
        super.render(); // important!

        if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
            debug = !debug;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            pauseGame(!pause);
        }

    }

    public void addPauseObserver(Observer observer){
        pauseObservable.addObserver(observer);
    }

    public void pauseGame(boolean pause) {
        this.pause = pause;
        pauseObservable.setChanged();
        pauseObservable.notifyObservers(pause);
    }

    public void exit(){
        Gdx.app.exit();
    }

    @Override
    public void dispose() {
        menuController.dispose();
        levelController.dispose();
    }

    public Set<Modifier> getModifiers() {
        return modifiers;
    }

    private static final GameController INSTANCE = new GameController();
    public static GameController getInstance() {
        return INSTANCE;
    }

    public GameController() {}

}