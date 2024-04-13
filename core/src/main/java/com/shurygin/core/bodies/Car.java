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

import static com.shurygin.core.bodies.FilterCategory.PLAYER;
import static com.shurygin.core.bodies.FilterCategory.SOLID;

public class Car extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("enemies/car.png"));
    private static float width = 2f * GameController.SIZE;
    private static float height = 1f * GameController.SIZE;
    private static float generalMaxSpeed = 3f;

    public Car() {

        super(new AnimationController(texture), ObjectType.ENEMY, -width, height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2f, height / 2f);

        body = bodyController.createBody(bodyDef);

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

}
