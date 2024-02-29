package com.shurygin.core.modifiers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.shurygin.core.AnimationController;
import com.shurygin.core.ContactListenerClass;
import com.shurygin.core.bodies.AbstractObject;
import com.shurygin.core.bodies.ObjectType;
import com.shurygin.core.bodies.Player;

import java.util.ArrayList;
import java.util.List;

import static com.shurygin.core.bodies.FilterCategory.*;

public class Viruses extends Modifier {

    public static final Texture textureLogo = new Texture(Gdx.files.internal("enemies/virus.png"));
    private List<Virus> viruses;
    private int n;

    public Viruses() {
        super();
        viruses = new ArrayList<>();
        n = 6;
    }

    @Override
    public void initialize() {
        for (int i = 0; i < n * amount; i++) {
            viruses.add(new Virus());
        }
    }

    @Override
    public void start() {
        for (Virus virus : viruses) {
            virus.start();
        }
    }

    @Override
    public AnimationController getLogoAnimation() {
        return new AnimationController(textureLogo);
    }

    public static class Virus extends AbstractObject {

        private static Texture texture = new Texture(Gdx.files.internal("enemies/virus.png"));
        private static float speed = 2f;
        private static float size = 0.75f;

        public Virus() {

            super(new AnimationController(texture), ObjectType.ENEMY, size);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
            bodyDef.fixedRotation = true;
            bodyDef.linearDamping = 0f;

            CircleShape shape = new CircleShape();
            shape.setRadius(width / 2 * 0.9f);

            body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();

            fixtureDef.shape = shape;

            fixtureDef.density = 10f;
            fixtureDef.friction = 0f;
            fixtureDef.restitution = 1f;

            fixtureDef.filter.categoryBits = SOLID.getN();
            fixtureDef.filter.maskBits = (short) (WALL.getN() | PLAYER.getN() | SOLID.getN());

            body.createFixture(fixtureDef).setUserData(this);
            shape.dispose();

            //createBody(bodyDef, shape, 10f, 0f, 1f);

            body.setTransform(Modifier.getRandomPosition(this), MathUtils.PI2 * MathUtils.random());

        }

        public void start() {
            Vector2 v = Vector2.X.setLength(speed).setAngleDeg(MathUtils.random(360f));
            body.setLinearVelocity(v);
            body.setAngularVelocity(MathUtils.random(-1f, 1f));
        }

        @Override
        public void touch(WorldManifold worldManifold, ObjectType type, AbstractObject object) {

            if (type == ObjectType.PLAYER && !((Player) object).isImmunity())
                gameScreen.death(this);
            else if (type == ObjectType.WALL)
                ContactListenerClass.handleRebound(worldManifold, body, true);
            else if (type == ObjectType.ENEMY)
                ContactListenerClass.handleRebound(worldManifold, body, false);
        }

        @Override
        public void update() {

            body.setLinearVelocity(body.getLinearVelocity().clamp(0f, speed));

        }

    }
}
