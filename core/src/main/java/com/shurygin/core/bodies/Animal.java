package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.GameController;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.ContactListenerClass;

import java.util.function.Supplier;

public class Animal extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("enemies/animal.png"));
    private static float size = 2f * GameController.SIZE;

    private float force = 30f;
    private float maxSpeed = 3.0f;
    private float coverage = 10f;
    private float frictionCoefficient = 0.975f;
    private float speed;
    private Vector2 direction;
    private Vector2 velocity;
    private boolean active;
    private boolean noticed;

    public Animal() {

        super(new AnimationController(texture), ObjectType.ENEMY, size * MathUtils.random(0.8f, 1.2f));
        bodyController.generatePosition(this);

        speed = maxSpeed * MathUtils.random(0.5f, 1.5f);
        noticed = false;

    }

    @Override
    protected BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        return bodyDef;
    }

    @Override
    protected Shape createShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2 * 0.7f);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = FilterCategory.SOLID;
        fixtureDef.filter.maskBits = (short) (FilterCategory.WALL | FilterCategory.PLAYER | FilterCategory.SOLID);
        return fixtureDef;
    }

    public void start() {
        active = true;
    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {
        if (type == ObjectType.PLAYER)
            bodyController.death(this);
        else if (type == ObjectType.WALL)
            ContactListenerClass.handleRebound(worldManifold, body, true);
        else if (type == ObjectType.ENEMY)
            ContactListenerClass.handleRebound(worldManifold, body, false);
    }

    @Override
    public void update() {
        if (!active) return;
        direction = new Vector2(player.getPosition().x - body.getPosition().x, player.getPosition().y - body.getPosition().y);
        noticed = direction.len() <= size / 2f + coverage;

        velocity = body.getLinearVelocity();

        velocity.x *= frictionCoefficient;
        velocity.y *= frictionCoefficient;
        velocity.clamp(0f, speed);

        body.setLinearVelocity(velocity);

        if (!noticed) return;
        direction.setLength(force);
        body.applyForceToCenter(direction, true);

    }

    @Override
    public Vector3 getStartTransform() {
        return getRandomTransform(false);
    }

}
