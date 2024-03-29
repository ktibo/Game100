package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.shurygin.core.AnimationController;
import com.shurygin.core.BorderController;

import java.util.List;

public class Wall extends AbstractObject {

    private static Texture texture = new Texture(Gdx.files.internal("wall.png"));

    public Wall(Vector2 position, float angle) {

        super(new AnimationController(texture, 3, 1),
                ObjectType.WALL,
                BorderController.getThickness(),
                BorderController.getLength());

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);
        bodyDef.angle = angle/MathUtils.radiansToDegrees;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BorderController.getThickness() / 2-0.04f, height / 2);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();

    }

}
