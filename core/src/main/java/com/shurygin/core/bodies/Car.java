package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.GameController;
import com.shurygin.core.utils.AnimationController;

import java.util.function.Supplier;

public class Car extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("enemies/car.png"));
    private static float width = 4f * GameController.SIZE;
    private static float height = 2f * GameController.SIZE;

    public Car() {

        super(new AnimationController(texture), ObjectType.ENEMY, -width, height);
        Vector2 pos = new Vector2();
        pos.x = GameController.WIDTH - Wall.getThickness() + width;
        pos.y = MathUtils.random(height / 2f, GameController.HEIGHT - height / 2f);
        body.setTransform(pos, 0f);

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
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f * 0.95f, height / 2f * 0.75f, new Vector2(0, 0.1f), 0f);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = FilterCategory.SOLID;
        fixtureDef.filter.maskBits = (short) (FilterCategory.PLAYER | FilterCategory.SOLID);
        return fixtureDef;
    }

    public void start() {

    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {
        if (type == ObjectType.PLAYER)
            bodyController.death(this);
        else if (type == ObjectType.ENEMY && object instanceof Animal)
            object.setNeedDestroy(true);
    }

    @Override
    public void update() {
        body.applyForceToCenter(-10f, 0f, true);
        if (body.getPosition().x < -2f) setNeedDestroy(true);
    }

    @Override
    public boolean avoidCollisionsAtBeginning() {
        return false;
    }

}
