package com.shurygin.core.modifiers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.bodies.AbstractObject;
import com.shurygin.core.bodies.ObjectType;
import com.shurygin.core.bodies.Player;
import com.shurygin.core.AnimationController;
import com.shurygin.core.ContactListenerClass;

import java.util.ArrayList;
import java.util.List;

import static com.shurygin.core.bodies.FilterCategory.*;

public class Rabies extends Modifier {

    public static final Texture textureLogo = new Texture(Gdx.files.internal("enemies/animal.png"));
    private List<Animal> list;
    private int n;

    public Rabies() {
        super();
        list = new ArrayList<>();
        n = 3;
    }

    @Override
    public void initialize() {
        for (int i = 0; i < n * amount; i++) {
            list.add(new Animal());
        }
    }

    @Override
    public void start() {
        for (Animal animal : list) {
            animal.start();
        }
    }

    @Override
    public AnimationController getLogoAnimation() {
        return new AnimationController(textureLogo);
    }

    public static class Animal extends AbstractObject {

        private static Texture texture = new Texture(Gdx.files.internal("enemies/animal.png"));
        private static float size = 1f;
        private static float force = 10f;
        private static Body playerBody;
        private static float maxSpeed = 1.5f;
        private static float coverage = 5f;
        private static float frictionCoefficient = 0.975f;

        private float speed;
        private Vector2 direction;
        private Vector2 velocity;
        private boolean active;
        private boolean noticed;

        public Animal() {

            super(new AnimationController(texture), ObjectType.ENEMY, size * MathUtils.random(0.8f, 1.2f));

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.fixedRotation = true;

            CircleShape shape = new CircleShape();
            shape.setRadius(width / 2 * 0.7f);

            body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();

            fixtureDef.shape = shape;

            fixtureDef.density = 0.5f;
            fixtureDef.friction = 0.5f;
            fixtureDef.restitution = 0.5f;
            fixtureDef.filter.categoryBits = SOLID.getN();
            fixtureDef.filter.maskBits = (short) (WALL.getN() | PLAYER.getN() | SOLID.getN());
            body.createFixture(fixtureDef).setUserData(this);
            shape.dispose();

            body.setTransform(Modifier.getRandomPosition(this), 0f);

            speed = maxSpeed * MathUtils.random(0.5f, 1.5f);
            noticed = false;

        }

        public void start() {
            playerBody = gameScreen.getPlayer().getBody();
            active = true;
        }

        @Override
        public void touch(WorldManifold worldManifold, ObjectType type, AbstractObject object) {
            if (type == ObjectType.PLAYER)
                gameScreen.death(this);
            else if (type == ObjectType.WALL)
                ContactListenerClass.handleRebound(worldManifold, body, true);
            else if (type == ObjectType.ENEMY)
                ContactListenerClass.handleRebound(worldManifold, body, false);
        }

        @Override
        public void update() {
            if (!active) return;
            direction = new Vector2(playerBody.getPosition().x - body.getPosition().x, playerBody.getPosition().y - body.getPosition().y);
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

    }
}
