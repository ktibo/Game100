package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.shurygin.core.GameController;
import com.shurygin.core.LevelController;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.BorderController;

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

        super(new AnimationController(texture, 3, 1), ObjectType.PLAYER, size);

        this.levelController = levelController;

        force = 200f;
        frictionCoefficient = 0.7f;
        maxSpeed = 20f;
        immunity = false;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2);

        body = bodyController.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;

        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;

        fixtureDef.filter.categoryBits = FilterCategory.PLAYER;
        fixtureDef.filter.maskBits = FilterCategory.ALL;

        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        body.setAngularVelocity(0f);
        bodyController.generatePosition(this);

    }

    @Override
    public void update() {

        velocity = body.getLinearVelocity();

        velocity.x *= frictionCoefficient;
        velocity.y *= frictionCoefficient;
        velocity.clamp(0f, maxSpeed);

        body.setLinearVelocity(velocity);

        mousePosition.set(Gdx.input.getX(), Gdx.input.getY());
        levelController.unproject(mousePosition);

        move(mousePosition);

    }

    private void move(Vector2 mousePosition) {

        if (!levelController.getActive()) {
            if (body.getFixtureList().get(0).testPoint(mousePosition)) {
                levelController.activate();
            } else {
                return;
            }
        }

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

        //body.applyForceToCenter(direction.x,direction.y,true);
        //body.setTransform(mousePosition, 0f);

    }

    @Override
    public Supplier<? extends Vector2> getGeneratePosition() {
        Vector2 startPosition = new Vector2(
                BorderController.getThickness() + size,
                BorderController.getThickness() + size);

        return () -> startPosition;
    }

    public void setImmunity(boolean immunity) {
        this.immunity = immunity;
    }

    public boolean isImmunity() {
        return immunity;
    }

}
