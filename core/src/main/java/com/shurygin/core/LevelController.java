package com.shurygin.core;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.shurygin.core.bodies.AbstractBody;
import com.shurygin.core.bodies.BodyController;
import com.shurygin.core.modifiers.Modifier;
import com.shurygin.core.utils.ContactListenerClass;

import java.util.Set;

public class LevelController {

    public static final float TIME_STEP = 1 / 120f;

    public World world;
    private GameController game;
    private LevelScreen levelScreen;
    private BodyController bodyController;
    private boolean active;

    private Set<Modifier> modifiers;

    public void startNewLevel() {

        world = new World(new Vector2(0, 0), true);
        world.setContactListener(new ContactListenerClass());
        active = false;

        GameController.changeScreen(levelScreen);

        bodyController.restart();

        modifiers = game.getModifiers();
        modifiers.forEach(Modifier::initialize);
    }

    public void update() {

        modifiers.forEach(Modifier::update);
        bodyController.update();
        world.step(TIME_STEP, 6, 2);

    }

    public void activate() {
        active = true;
        modifiers.forEach(Modifier::start);
    }

    public void completeLevel() {
        terminateLevel();
        game.completeLevel();
    }

    public void death(AbstractBody enemy) {
        game.pauseGame(true);
        //terminateLevel();
        System.out.println("DEATH! By " + enemy);
        game.death();
    }

    private void terminateLevel() {
        modifiers.forEach(Modifier::stop);
        bodyController.clear();
    }

    public void render(float delta) {
        bodyController.render(delta);
    }

    public void unproject(Vector2 mousePosition) {
        levelScreen.unproject(mousePosition);
    }

    public World getWorld() {
        return world;
    }

    public void dispose() {
        if (world != null) {
            world.dispose();
        }
        levelScreen.dispose();
    }

    public boolean getActive(){
        return active;
    }

    public LevelController(GameController gameController) {
        game = gameController;
        levelScreen = new LevelScreen(this);
        bodyController = new BodyController(this);
    }
}
