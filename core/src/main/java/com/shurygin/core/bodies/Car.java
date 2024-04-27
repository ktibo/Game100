package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.BorderController;
import com.shurygin.core.GameController;

import java.util.function.Supplier;

public class Car extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("enemies/car.png"));
    private static float width = 4f * GameController.SIZE;
    private static float height = 2f * GameController.SIZE;

    public Car() {

        super(new AnimationController(texture), ObjectType.ENEMY, -width, height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        //shape.setAsBox(width / 2f * 0.95f, height / 2f * 0.9f);
        shape.setAsBox(width / 2f * 0.95f, height / 2f * 0.75f, new Vector2(0, 0.1f), 0f);

        body = bodyController.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;

        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = FilterCategory.SOLID;
        fixtureDef.filter.maskBits = (short) (FilterCategory.PLAYER | FilterCategory.SOLID);
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        body.setTransform(getGeneratePosition().get(), 0f);
        //bodyController.generatePosition(this);

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
    public Supplier<? extends Vector2> getGeneratePosition() {
        Vector2 pos = new Vector2();
        pos.x = GameController.WIDTH - BorderController.getThickness() + width;
        pos.y = MathUtils.random(height / 2f, GameController.HEIGHT - height / 2f);

        return () -> pos;
    }

}
