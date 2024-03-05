package com.shurygin.core.modifiers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.Timer;
import com.shurygin.core.AnimationController;
import com.shurygin.core.BorderController;
import com.shurygin.core.GameController;
import com.shurygin.core.bodies.AbstractObject;
import com.shurygin.core.bodies.ObjectType;

import static com.shurygin.core.bodies.FilterCategory.PLAYER;
import static com.shurygin.core.bodies.FilterCategory.SOLID;

public class TrafficCollision extends Modifier {

    public static final Texture textureLogo = new Texture(Gdx.files.internal("enemies/car.png"));
    Timer timer;
    private float delay;

    public TrafficCollision() {
        super();
    }

    @Override
    public void initialize() {

        timer = new Timer();
        delay = 2f / (float) amount;

        timer.scheduleTask(new Timer.Task() {
                               @Override
                               public void run() {
                                   if (gameScreen.isPaused()) return;
                                   new Car().start();
                               }
                           }, MathUtils.random(delay), delay
        );

        timer.stop();

    }

    @Override
    public void start() {
        timer.start();
    }

    @Override
    public void stop() {
        timer.clear();
    }

    @Override
    public AnimationController getLogoAnimation() {
        return new AnimationController(textureLogo);
    }

    public static class Car extends AbstractObject {

        private static Texture texture = new Texture(Gdx.files.internal("enemies/car.png"));
        private static float width = 2f * GameController.SIZE;
        private static float height = 1f * GameController.SIZE;
        private static float generalMaxSpeed = 3f;

        private float maxSpeed;

        public Car() {

            super(new AnimationController(texture), ObjectType.ENEMY, -width, height);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.fixedRotation = true;

            PolygonShape shape = new PolygonShape();
            shape.setAsBox(width / 2f, height / 2f);

            body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();

            fixtureDef.shape = shape;

            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = SOLID.getN();
            fixtureDef.filter.maskBits = (short) (PLAYER.getN() | SOLID.getN());
            body.createFixture(fixtureDef).setUserData(this);
            shape.dispose();

            Vector2 pos = new Vector2();
            pos.x = GameController.WIDTH - BorderController.getThickness() + width;
            pos.y = MathUtils.random(height / 2f, GameController.HEIGHT - height / 2f);

            body.setTransform(pos, 0f);

            maxSpeed = generalMaxSpeed * MathUtils.random(0.5f, 1.5f);

        }

        public void start() {

        }

        @Override
        public void touch(WorldManifold worldManifold, ObjectType type, AbstractObject object) {
            if (type == ObjectType.PLAYER)
                gameScreen.death(this);
//            else if (type == ObjectType.WALL)
//                ContactListenerClass.handleRebound(worldManifold, object.getBody(), true);
            else if (type == ObjectType.ENEMY && object instanceof Rabies.Animal)
                object.setNeedRemove(true);
        }

        @Override
        public void update() {

            body.applyForceToCenter(-10f, 0f, true);
//            if (!active) return;
//            direction = new Vector2(playerBody.getPosition().x - body.getPosition().x, playerBody.getPosition().y - body.getPosition().y);
//            noticed = direction.len() <= size/2f + coverage;
//
//            velocity = body.getLinearVelocity();
//
//            velocity.x *= frictionCoefficient;
//            velocity.y *= frictionCoefficient;
//            velocity.clamp(0f, maxSpeed);
//
//            body.setLinearVelocity(velocity);
//
//            if (!noticed) return;
//            direction.setLength(force);
//            body.applyForceToCenter(direction, true);

            if (body.getPosition().x < -2f) needRemove = true;

        }

    }
}
