package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.GameController;

import java.util.function.Supplier;

public class Syringe extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("syringe.png"));
    private static float size = 1f * GameController.SIZE;

    private boolean active;

    public Syringe() {

        super(new AnimationController(texture), ObjectType.COLLECTABLE, size);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2 * 0.9f);

        body = bodyController.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;

        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = FilterCategory.SENSOR;
        fixtureDef.filter.maskBits = (short) (FilterCategory.WALL | FilterCategory.PLAYER);
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        //body.setTransform(BodyController.getRandomPosition(this), MathUtils.PI2 * MathUtils.random());
        bodyController.generatePosition(this);

        active = true;
        bodyController.getTarget().addActivation();

    }

    public void start() {

    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {

        if (object.getType() != ObjectType.PLAYER) return;
        if (!active) return;

        //currentAnimation = null;
        bodyController.getTarget().subtractActivation();
        bodyController.getPlayer().setImmunity(true);
        active = false;
        setNeedDestroy(true);

    }

    @Override
    public void update() {

    }

    @Override
    public Supplier<? extends Vector2> getGeneratePosition() {
        return getRandomGeneratePosition();
    }

}
