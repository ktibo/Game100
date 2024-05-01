package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.shurygin.core.GameController;
import com.shurygin.core.utils.AnimationController;

import java.util.function.Supplier;

public class Cigarette extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("tobacco.png"));
    private static float size = 1f * GameController.SIZE;

    private float coverage = 10f;
    private float rotationSpeed;
    private Vector2 direction;
    private float force = 50f;

    public Cigarette() {
        super(new AnimationController(texture), ObjectType.COLLECTABLE, size);
    }

    @Override
    protected BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        return bodyDef;
    }

    @Override
    protected Shape createShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2 * 0.9f);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = FilterCategory.SENSOR;
        fixtureDef.filter.maskBits = FilterCategory.WALL;
        return fixtureDef;
    }

    public void start() {
        rotationSpeed = MathUtils.random(-0.01f, 0.01f);
    }

    @Override
    public void update() {

        body.setTransform(body.getPosition(), body.getAngle()+rotationSpeed);

        direction = new Vector2(body.getPosition().x - player.getPosition().x,body.getPosition().y - player.getPosition().y);
        float zone = size / 2f + coverage - direction.len();

        if (zone > 0) {
            float f = Math.min(zone * force, 100f);
            direction.setLength(f);
            player.getBody().applyForceToCenter(direction, true);
        }
    }

    @Override
    public Supplier<? extends Vector3> getGeneratePosition() {
        return getRandomGeneratePosition(true);
    }

}
