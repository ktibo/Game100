package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.GameController;
import com.shurygin.core.bodies.utils.AbstractBody;
import com.shurygin.core.bodies.utils.FilterCategory;
import com.shurygin.core.bodies.utils.ObjectType;
import com.shurygin.core.modifiers.Diabetes;
import com.shurygin.core.utils.AnimationController;

public class Syringe extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("syringe.png"));
    private static float size = 1f * GameController.SIZE;
    private final Diabetes diabetes;

    private boolean active;
    private float rotationSpeed;

    public Syringe(Diabetes diabetes) {

        super(new AnimationController(texture), ObjectType.COLLECTABLE, size);
        this.diabetes = diabetes;
        bodyController.generatePosition(this);

        active = true;
//        bodyController.getTarget().addActivation();
        rotationSpeed = MathUtils.random(-0.01f, 0.01f);
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

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {

        if (object.getType() != ObjectType.PLAYER) return;
        if (!active) return;

        diabetes.setImmunity(true);
        diabetes.subtractActivation();
        active = false;
        setNeedDestroy(true);

    }

    @Override
    public void update() {
        body.setTransform(body.getPosition(), body.getAngle() + rotationSpeed);
    }

    @Override
    public Vector3 getStartTransform() {
        return getRandomTransform(true);
    }

}
