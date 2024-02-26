package com.shurygin.core.modifiers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.shurygin.core.AnimationController;
import com.shurygin.core.bodies.AbstractObject;
import com.shurygin.core.bodies.ObjectType;

import static com.shurygin.core.bodies.FilterCategory.*;

public class Diabetes extends Modifier {

    public static final Texture textureLogo = new Texture(Gdx.files.internal("syringe.png"));

    public Diabetes() {
        super();
    }

    @Override
    public void initialize() {
        for (int i = 0; i < amount; i++) {
            new Syringe();
            gameScreen.getTarget().addActivation();
        }
    }

    @Override
    public void start() {
    }

    @Override
    public AnimationController getLogoAnimation() {
        return new AnimationController(textureLogo);
    }

    public static class Syringe extends AbstractObject {

        private static Texture texture = new Texture(Gdx.files.internal("syringe.png"));
        private static float size = 1f;

        private boolean active;

        public Syringe() {

            super(new AnimationController(texture), ObjectType.COLLECTABLE, size);

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.KinematicBody;

            CircleShape shape = new CircleShape();
            shape.setRadius(width / 2 * 0.9f);

            body = world.createBody(bodyDef);

            FixtureDef fixtureDef = new FixtureDef();

            fixtureDef.shape = shape;

            fixtureDef.isSensor = true;
            fixtureDef.filter.categoryBits = SENSOR.getN();
            fixtureDef.filter.maskBits = (short) (WALL.getN() | PLAYER.getN());
            body.createFixture(fixtureDef).setUserData(this);
            shape.dispose();

            body.setTransform(Modifier.getRandomPosition(this), MathUtils.PI2 * MathUtils.random());

            active = true;

        }

        public void start() {

        }

        @Override
        public void touch(WorldManifold worldManifold, ObjectType type, AbstractObject object) {

            if (object.getType() != ObjectType.PLAYER) return;
            if (!active) return;

            //currentAnimation = null;
            gameScreen.getTarget().subtractActivation();
            gameScreen.getPlayer().setImmunity(true);
            active = false;
            setNeedRemove(true);

        }

        @Override
        public void update() {

        }

    }
}
