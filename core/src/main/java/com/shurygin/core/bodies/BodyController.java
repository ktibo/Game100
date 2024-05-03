package com.shurygin.core.bodies;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.shurygin.core.LevelController;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class BodyController {

    private static BodyController INSTANCE;

    private final LevelController levelController;
    private final Set<AbstractBody> bodies = new TreeSet<>();

    private Target target;
    private Player player;
    private BorderBody playerBorder;
    private BorderBody targetBorder;

    public void update() {
        AbstractBody object;
        Iterator<AbstractBody> iterator = bodies.iterator();
        while (iterator.hasNext()) {
            object = iterator.next();
            if(object.isNeedDestroy()){
                object.destroy();
                levelController.getWorld().destroyBody(object.getBody());
                iterator.remove();
            } else {
                object.update();
            }
        }
    }

    public void render(float delta) {
        bodies.forEach(abstractBody -> abstractBody.render(delta));
    }

    public void restart() {

        clear();

        Wall.setWalls();

        player = new Player(levelController);
        target = new Target(levelController);

        playerBorder = new BorderBody(5f, BodyController.getInstance().getPlayer().getPosition());
        targetBorder = new BorderBody(5f, BodyController.getInstance().getTarget().getPosition());

    }

    public Player getPlayer(){
        return INSTANCE.player;
    }
    public Target getTarget() {
        return INSTANCE.target;
    }

    public Body createBody(BodyDef bodyDef) {
        return levelController.getWorld().createBody(bodyDef);
    }

    public void death(AbstractBody enemy) {
        INSTANCE.levelController.death(enemy);
    }

    public void clear() {
        bodies.clear();
    }

    public void addBody(AbstractBody body) {
        bodies.add(body);
    }

    public static BodyController getInstance(){
        if (INSTANCE == null) throw new NullPointerException("BodyController is null!");
        return INSTANCE;
    }
    public BodyController(LevelController levelController) {
        if (INSTANCE != null) throw new RuntimeException("BodyController is not null!");
        this.levelController = levelController;
        INSTANCE = this;
    }

    public void generatePosition(AbstractBody body) {

        // Concern about objects don't collide with each other

        int attempts = 100;
        World world = levelController.getWorld();
        Vector3 pos;
        do {
            pos = body.getTransform();
            body.getBody().setTransform(pos.x, pos.y, pos.z);
            if (!body.avoidCollisionsAtBeginning())
                return;
            world.step(LevelController.TIME_STEP, 6, 10);
        } while (world.getContactCount() > 0 && attempts-- > 0);

        if (attempts < 95) System.err.println("attempts left: "+attempts);

    }

    public void activate() {
        playerBorder.setNeedDestroy(true);
        targetBorder.setNeedDestroy(true);
        getPlayer().activate();
    }
}
