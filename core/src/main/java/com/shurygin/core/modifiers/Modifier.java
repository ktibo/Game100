package com.shurygin.core.modifiers;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.shurygin.core.bodies.AbstractObject;
import com.shurygin.core.bodies.ObjectType;
import com.shurygin.core.bodies.Player;
import com.shurygin.core.bodies.Target;
import com.shurygin.core.AnimationController;
import com.shurygin.core.Constants;
import com.shurygin.core.Texts;
import com.shurygin.core.screens.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Modifier {

    protected int amount;

    public static List<Modifier> getAllModifiers() {

        List<Modifier> allModifiers = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            allModifiers.add(new Diabetes());
            allModifiers.add(new Viruses());
            allModifiers.add(new Rabies());
            allModifiers.add(new TrafficCollision());
        }

        Collections.shuffle(allModifiers);

        return allModifiers;

    }

    protected GameScreen gameScreen;

    Modifier() {
        amount = 0;
        gameScreen = GameScreen.getInstance();
    }

    public final void increaseAmount() {
        amount++;
    }

    public abstract AnimationController getLogoAnimation();

    public abstract void initialize();

    public abstract void start();

    public void update() {

    }

    ;

    public void stop() {

    }

    public String getTitle() {
        String key = getClass().getSimpleName() + "_title";
        return Texts.get(key);
    }

    public String getDescription() {
        String key = getClass().getSimpleName() + "_description";
        return Texts.get(key);
    }

    public static Vector2 getRandomPosition(AbstractObject object) {

        List<AbstractObject> allObjects = new ArrayList<>(GameScreen.getInstance().getBodies());
        allObjects.remove(object);

        float width = object.getWidth();
        float height = object.getHeight();
        float size = Math.max(width, height);
        Vector2 pos = new Vector2();
        int attempts = 1000;

        do {
            pos.x = MathUtils.random(width / 2, Constants.WIDTH - width / 2);
            pos.y = MathUtils.random(height / 2, Constants.HEIGHT - height / 2);
            attempts--;
        } while (attempts > 0 && !positionIsCorrect(pos, size, allObjects));

        Body body = object.getBody();
        body.setTransform(pos, body.getAngle());

        return pos;

    }

    private static boolean positionIsCorrect(Vector2 pos, float size, List<AbstractObject> allObjects) {

        float maxPlayerDistance = Player.size / 2f + size / 2f + Player.size;
        float maxTargetDistance = Target.size / 2f + size / 2f + Target.size / 2f;
        float maxDistance; // between other bodies

        if (pos.dst(Player.startPosition) < maxPlayerDistance) return false; // too close to player
        if (pos.dst(Target.startPosition) < maxTargetDistance) return false; // too close to target

        for (AbstractObject object : allObjects) {

            if (object.getType() != ObjectType.ENEMY && object.getType() != ObjectType.COLLECTABLE) continue;

            float objectSize = Math.max(object.getWidth(), object.getHeight());
            maxDistance = size / 2f + objectSize / 2f + (size + objectSize) / 8f;

            if (pos.dst(object.getPosition()) < maxDistance) return false; // too close to another body

        }

        return true;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modifier)) return false;

        Modifier modifier = (Modifier) o;

        return Objects.equals(this.getClass(), modifier.getClass());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}
