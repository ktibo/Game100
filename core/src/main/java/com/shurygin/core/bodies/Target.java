package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.shurygin.core.*;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.BorderController;

import java.util.function.Supplier;

public class Target extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("cake.png"));

    public static float size = 1.0f * GameController.SIZE; // width and height

    private LevelController levelController;
    private int activations;

    public void addActivation() {
        activations++;
    }

    public void subtractActivation() {
        activations--;
    }

    public Target(LevelController levelController) {

        super(new AnimationController(texture, 1, 1), ObjectType.COLLECTABLE, size);

        this.levelController = levelController;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body = bodyController.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;

        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        //body.setTransform(startPosition, 0f);
        bodyController.generatePosition(this);

        activations = 0;

    }

    @Override
    public void render(float delta) {

        Color c = LevelScreen.BATCH.getColor();
        if (activations > 0) LevelScreen.BATCH.setColor(c.r, c.g, c.b, 0.3f);
        super.render(delta);
        LevelScreen.BATCH.setColor(c.r, c.g, c.b, 1f);

    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {

        if (object.getType() != ObjectType.PLAYER) return;
        if (activations > 0) return;

        levelController.completeLevel();
    }

    @Override
    public Supplier<? extends Vector2> getGeneratePosition() {
        Vector2 startPosition = new Vector2(
                GameController.WIDTH - BorderController.getThickness() - size,
                GameController.HEIGHT - BorderController.getThickness() - size);

        return () -> startPosition;
    }

}
