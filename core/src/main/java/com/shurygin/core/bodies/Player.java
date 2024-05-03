package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.shurygin.core.GameController;
import com.shurygin.core.LevelController;
import com.shurygin.core.utils.AnimationController;

import java.util.function.Supplier;

public class Player extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("player.png"));
    public static float size = 1f * GameController.SIZE; // width and height
    private static float force;
    private static float frictionCoefficient;
    private static float maxSpeed;

    private LevelController levelController;
    private Vector2 mousePosition = new Vector2();
    private Vector2 position;
    private Vector2 direction;
    private Vector2 velocity;
    private boolean immunity;

    public Player(LevelController levelController) {

        super(new AnimationController(texture), ObjectType.PLAYER, size);
        bodyController.generatePosition(this);

        this.levelController = levelController;
        force = 200f;
        frictionCoefficient = 0.7f;
        maxSpeed = 10f;
        immunity = false;
        body.setAngularVelocity(0f);
        body.setActive(false);

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
        shape.setRadius(width / 2);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = FilterCategory.PLAYER;
        fixtureDef.filter.maskBits = FilterCategory.ALL;
        return fixtureDef;
    }

    @Override
    public void update() {

        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        levelController.unproject(mousePosition);

        if (!levelController.getActive()) {
            if (body.getFixtureList().get(0).testPoint(mousePosition)) {
                levelController.activate();
            } else {
                return;
            }
        }

        velocity = body.getLinearVelocity();

        velocity.x *= frictionCoefficient;
        velocity.y *= frictionCoefficient;
        velocity.clamp(0f, maxSpeed);

        body.setLinearVelocity(velocity);

        move(mousePosition);

    }

    private void move(Vector2 mousePosition) {

        position = body.getPosition();

        direction = new Vector2(mousePosition.x - position.x, mousePosition.y - position.y);

        float len = direction.len();
        float newLen = 0f;
        if (len > 0.02f) {
            newLen = (float) Math.sqrt(Math.sqrt(len));
        }
        float ratio = newLen / len;

        body.applyForceToCenter(direction.x * ratio * force,
                direction.y * ratio * force,
                true);

    }

    @Override
    public Vector3 getTransform() {
        Vector3 startPosition = new Vector3();
        startPosition.x = Wall.getThickness() + size;
        startPosition.y = Wall.getThickness() + size;
        startPosition.z = 0f;
        return startPosition;
    }

    public void setImmunity(boolean immunity) {
        this.immunity = immunity;
    }

    public boolean isImmunity() {
        return immunity;
    }

    public void activate() {
        body.setActive(true);
    }

}
