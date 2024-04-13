package com.shurygin.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Observable;
import java.util.Observer;

import static com.shurygin.core.LevelController.TIME_STEP;

public class LevelScreen implements Screen, Observer {

    public static final Batch BATCH = new SpriteBatch();

    private final LevelController levelController;
    private final Viewport viewport;
    private final OrthographicCamera camera;

    private Texture background = new Texture(Gdx.files.internal("background.png"));
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    private boolean pause = false;

    //private Sound dropSound;
    //private Music music;

    private float accumulator = 0;

    public LevelScreen(LevelController levelController) {

        this.levelController = levelController;

        GameController game = GameController.getInstance();
        viewport = game.getViewport();
        camera = game.getCamera();

        GameController.getInstance().addPauseObserver(this);

//        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
//        music = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
//        music.setLooping(true);

    }

    @Override
    public void render(float delta) {

        //ScreenUtils.clear(screenColor);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        BATCH.begin();

        BATCH.draw(background, 0f, 0f, GameController.WIDTH, GameController.HEIGHT);

        //

        levelController.render(delta);

        BATCH.end();

        //if (levelController.isPaused()) return;
        if (pause) return;

        if (GameController.debug) {
            debugRenderer.render(levelController.getWorld(), camera.combined);
        }

        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            levelController.update();
            camera.update();
            BATCH.setProjectionMatrix(camera.combined);
            accumulator -= TIME_STEP;
        }

    }

    public void unproject(Vector2 mousePosition) {
        viewport.unproject(mousePosition);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        accumulator = 0f;
        //rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        GameController.getInstance().pauseGame(true);
    }

    @Override
    public void resume() {
        GameController.getInstance().pauseGame(false);
    }

    @Override
    public void dispose() {
//        dropSound.dispose();
//        music.dispose();
        background.dispose();
        BATCH.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
        pause = (boolean)arg;
    }
}
