package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.GameController;
import com.shurygin.core.utils.AnimationController;

import java.util.function.Supplier;

public class Syringe extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("syringe.png"));
    private static float size = 1f * GameController.SIZE;

    private boolean active;

    public Syringe() {

        super(new AnimationController(texture), ObjectType.COLLECTABLE, size);
        bodyController.generatePosition(this);

        active = true;
        bodyController.getTarget().addActivation();

    }

    @Override
    protected BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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
        fixtureDef.filter.maskBits = (short) (FilterCategory.WALL | FilterCategory.PLAYER);
        return fixtureDef;
    }

    public void start() {

    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {

        if (object.getType() != ObjectType.PLAYER) return;
        if (!active) return;

        bodyController.getTarget().subtractActivation();
        bodyController.getPlayer().setImmunity(true);
        active = false;
        setNeedDestroy(true);

    }

    @Override
    public void update() {

    }

    @Override
    public Vector3 getTransform() {
        return getRandomTransform(true);
    }

}
