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
    //private boolean immunity;

    public Player(LevelController levelController) {

        super(new AnimationController(texture), ObjectType.PLAYER, size);
        bodyController.generatePosition(this);

        this.levelController = levelController;
        force = 200f;
        frictionCoefficient = 0.7f;
        maxSpeed = 10f;
        body.setAngularVelocity(0f);
        body.setActive(false);
        position = body.getPosition();

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

        if (!levelController.isActive()) {
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

        float len = Math.min(1.0f, direction.len());
        direction.setLength(1f);

        body.applyForceToCenter(direction.x * len * force,
                direction.y * len * force,
                true);

    }

    @Override
    public Vector3 getStartTransform() {
        Vector3 startPosition = new Vector3();
        startPosition.x = Wall.getThickness() + size;
        startPosition.y = Wall.getThickness() + size;
        startPosition.z = 0f;
        return startPosition;
    }

    public void activate() {
        body.setActive(true);
    }

}
