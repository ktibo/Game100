package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.ContactListenerClass;
import com.shurygin.core.GameController;

import java.util.function.Supplier;

public class Virus extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("enemies/virus.png"));
    private static float speed = 2f;
    private static float size = 1.5f * GameController.SIZE;

    public Virus() {

        super(new AnimationController(texture), ObjectType.ENEMY, size);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0f;

        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2 * 0.85f);

        body = bodyController.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;

        fixtureDef.density = 10f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;

        fixtureDef.filter.categoryBits = FilterCategory.SOLID;
        fixtureDef.filter.maskBits = (short) (FilterCategory.WALL | FilterCategory.PLAYER | FilterCategory.SOLID);

        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();

        //createBody(bodyDef, shape, 10f, 0f, 1f);

        //body.setTransform(BodyController.getRandomPosition(this), MathUtils.PI2 * MathUtils.random());
        bodyController.generatePosition(this);
        body.setTransform(body.getPosition(), MathUtils.PI2 * MathUtils.random());

    }

    public void start() {
        Vector2 v = Vector2.X.setLength(speed).setAngleDeg(MathUtils.random(360f));
        body.setLinearVelocity(v);
        body.setAngularVelocity(MathUtils.random(-1f, 1f));
    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {

        if (type == ObjectType.PLAYER && !((Player) object).isImmunity())
            bodyController.death(this);
        else if (type == ObjectType.WALL)
            ContactListenerClass.handleRebound(worldManifold, body, true);
        else if (type == ObjectType.ENEMY)
            ContactListenerClass.handleRebound(worldManifold, body, false);
    }

    @Override
    public void update() {
        body.setLinearVelocity(body.getLinearVelocity().clamp(speed, speed));
    }

    @Override
    public Supplier<? extends Vector2> getGeneratePosition() {
        return getRandomGeneratePosition();
    }

}
