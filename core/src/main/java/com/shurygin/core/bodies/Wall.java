package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.shurygin.core.AnimationController;

public class Wall extends AbstractObject {

    private static Texture texture = new Texture(Gdx.files.internal("wall.png"));

    public Wall(float width, float height, Vector2 position) {

        super(new AnimationController(texture, 1, 1), ObjectType.WALL, width, height);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(position);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2);

        body = world.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();

    }

}
