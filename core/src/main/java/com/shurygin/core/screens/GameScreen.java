package com.shurygin.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shurygin.core.BorderController;
import com.shurygin.core.bodies.AbstractObject;
import com.shurygin.core.bodies.Player;
import com.shurygin.core.bodies.Target;
import com.shurygin.core.bodies.Wall;
import com.shurygin.core.ContactListenerClass;
import com.shurygin.core.GameController;
import com.shurygin.core.modifiers.Modifier;

import java.util.*;

public class GameScreen implements Screen {

    private static final float TIME_STEP = 1 / 120f;
    private static GameScreen instance;

    private GameController game;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final Batch batch;
    private World world;

    public World getWorld() {
        return world;
    }

    public Batch getBatch() {
        return batch;
    }


    private Texture background = new Texture(Gdx.files.internal("background.png"));
    private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();

    private Sound dropSound;
    private Music music;

    private Set<AbstractObject> bodies;

    public Set<AbstractObject> getBodies() {
        return bodies;
    }

    private Set<Modifier> modifiers;

    private Player player;

    public Player getPlayer() {
        return player;
    }

    private Target target;

    public Target getTarget() {
        return target;
    }

    private float accumulator = 0;

    private boolean isPaused;

    public boolean isPaused() {
        return isPaused;
    }

    private boolean isFinish;

    public static GameScreen getInstance() {
        if (instance == null) instance = new GameScreen();
        return instance;
    }

    private GameScreen() {

        game = GameController.getInstance();

        camera = game.getCamera();
        viewport = game.getViewport();

        batch = new SpriteBatch();

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        music = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        music.setLooping(true);

        //Gdx.input.setInputProcessor(GameInputAdapter.getInstance());

    }

    public void startLevel() {

        accumulator = 0f;
        isPaused = isFinish = false;

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListenerClass());
        //world.setContactFilter();
        bodies = new TreeSet<>(new Comparator<AbstractObject>() {
            @Override
            public int compare(AbstractObject o1, AbstractObject o2) {
                return o1.getDepth() == o2.getDepth()? o1.hashCode() - o2.hashCode() : o1.getDepth() - o2.getDepth();
            }
        });

        BorderController.setBorders();

        target = new Target();
        player = new Player();

        modifiers = game.getCurrentModifiers();
        for (Modifier modifier : modifiers) {
            modifier.initialize();
        }

    }

    public void finishLevel() {
        isFinish = true;
        for (Modifier modifier : modifiers) {
            modifier.stop();
        }
        game.finishLevel();
    }

    public void death(AbstractObject enemy) {
        pause();
        System.out.println("DEATH! By " + enemy);
        game.death();
    }

    private void terminateLevel() {
        if (world != null) {
            world.dispose();
        }
    }

    @Override
    public void render(float delta) {

        if (isFinish) {
            terminateLevel();
            return;
        }

        //ScreenUtils.clear(screenColor);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        batch.draw(background, 0f, 0f, GameController.WIDTH, GameController.HEIGHT);

        for (AbstractObject object : bodies) {
            object.render(delta);
        }

        //BorderController.draw(delta);

        batch.end();

        if (isPaused) return;

        if (GameController.debug) {
            debugRenderer.render(world, camera.combined);
        }

        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(delta, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            update();
            accumulator -= TIME_STEP;
        }
    }

    void update() {

        for (Modifier modifier : modifiers) {
            modifier.update();
        }

        AbstractObject object;
        Iterator<AbstractObject> iterator = bodies.iterator();
        while (iterator.hasNext()) {
            object = iterator.next();
            if(object.isNeedRemove()){
                object.remove();
                iterator.remove();
            } else {
                object.update();
            }
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        world.step(TIME_STEP, 6, 2);

    }

    public void activate() {
        for (Modifier modifier : modifiers) {
            modifier.start();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        //rainMusic.play();
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
        isPaused = !isPaused;
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        dropSound.dispose();
        music.dispose();
        if (world != null) {
            world.dispose();
        }
        background.dispose();
        batch.dispose();
    }

    //public void cancelPause() {
    //    isPaused = false;
    //}

}
