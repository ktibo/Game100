package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.GameController;
import com.shurygin.core.LevelController;
import com.shurygin.core.LevelScreen;
import com.shurygin.core.bodies.utils.AbstractBody;
import com.shurygin.core.bodies.utils.ObjectType;
import com.shurygin.core.utils.AnimationController;

public class Target extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("cake.png"));

    public static float size = 1.0f * GameController.SIZE; // width and height

    private LevelController levelController;
    private boolean active;

    public Target(LevelController levelController) {
        super(new AnimationController(texture, 1, 1), ObjectType.COLLECTABLE, size);
        bodyController.generatePosition(this);
        this.levelController = levelController;
        setActive(true);
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    protected BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.fixedRotation = true;
        return bodyDef;
    }

    @Override
    protected Shape createShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        return fixtureDef;
    }

    @Override
    public void render(float delta) {

        if (active) {
            super.render(delta);
        } else { // semitransparent
            Color c = LevelScreen.BATCH.getColor();
            LevelScreen.BATCH.setColor(c.r, c.g, c.b, 0.3f);
            super.render(delta);
            LevelScreen.BATCH.setColor(c.r, c.g, c.b, 1f);
        }

    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {
        if (object.getType() != ObjectType.PLAYER || !active) return;
        levelController.completeLevel();
    }

    @Override
    public Vector3 getStartTransform() {

        Vector3 startPosition = new Vector3();
        startPosition.x = GameController.WIDTH - Wall.getThickness() - size;
        startPosition.y = GameController.HEIGHT - Wall.getThickness() - size;
        startPosition.z = 0f;
        return startPosition;

    }

}
