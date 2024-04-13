package com.shurygin.core.bodies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.shurygin.core.utils.BorderController;
import com.shurygin.core.GameController;
import com.shurygin.core.LevelController;

import java.util.*;

public class BodyController {

    private static BodyController INSTANCE;

    private final LevelController levelController;
    private final Set<AbstractBody> bodies = new TreeSet<>();

    private Target target;
    private Player player;

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
        for (AbstractBody object : bodies) {
            object.render(delta);
        }
    }

    public void restart() {

        clear();

        target = new Target(levelController);
        player = new Player(levelController);

        BorderController.setBorders();

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

    public static Vector2 getRandomPosition(AbstractBody object) {

        float wallThickness = BorderController.getThickness();
        float width = object.getWidth();
        float height = object.getHeight();
        Vector2 pos = new Vector2();

        pos.x = MathUtils.random(wallThickness + width / 2, GameController.WIDTH - wallThickness - width / 2);
        pos.y = MathUtils.random(wallThickness + height / 2, GameController.HEIGHT - wallThickness - height / 2);

        return pos;

    }

    public void addBody(AbstractBody body) {
        bodies.add(body);
    }

//    public static Vector2 getRandomPosition(AbstractBody object) {
//
//        List<AbstractBody> allObjects = new ArrayList<>(.getBodies());
//        allObjects.remove(object);
//
//        float wallThickness = BorderController.getThickness();
//        float width = object.getWidth();
//        float height = object.getHeight();
//        float size = Math.max(width, height);
//        Vector2 pos = new Vector2();
//        int attempts = 1000;
//
//
//        do {
//            pos.x = MathUtils.random(wallThickness + width / 2, GameController.WIDTH - wallThickness - width / 2);
//            pos.y = MathUtils.random(wallThickness + height / 2, GameController.HEIGHT - wallThickness - height / 2);
//            attempts--;
//        } while (attempts > 0 && !positionIsCorrect(pos, size, allObjects));
//
//        Body body = object.getBody();
//        body.setTransform(pos, body.getAngle());
//
//        return pos;
//
//    }

//    private static boolean positionIsCorrect(Vector2 pos, float size, List<AbstractBody> allObjects) {
//
//        float maxPlayerDistance = Player.size / 2f + size / 2f + Player.size;
//        float maxTargetDistance = Target.size / 2f + size / 2f + Target.size / 2f;
//        float maxDistance; // between other bodies
//
//        if (pos.dst(Player.startPosition) < maxPlayerDistance) return false; // too close to player
//        if (pos.dst(Target.startPosition) < maxTargetDistance) return false; // too close to target
//
//        for (AbstractBody object : allObjects) {
//
//            if (object.getType() != ObjectType.ENEMY && object.getType() != ObjectType.COLLECTABLE) continue;
//
//            float objectSize = Math.max(object.getWidth(), object.getHeight());
//            maxDistance = size / 2f + objectSize / 2f + (size + objectSize) / 8f;
//
//            if (pos.dst(object.getPosition()) < maxDistance) return false; // too close to another body
//
//        }
//
//        return true;
//
//    }

    public static BodyController getInstance(){
        if (INSTANCE == null) throw new NullPointerException("BodyController is null!");
        return INSTANCE;
    }
    public BodyController(LevelController levelController) {
        if (INSTANCE != null) throw new RuntimeException("BodyController is not null!");
        this.levelController = levelController;
        INSTANCE = this;
    }

}
