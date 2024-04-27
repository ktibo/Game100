package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.BorderController;

import java.util.function.Supplier;

public class Wall extends AbstractBody {

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

        body = bodyController.createBody(bodyDef);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = FilterCategory.WALL;
        fixtureDef.filter.maskBits = (short) (FilterCategory.PLAYER | FilterCategory.SOLID);

        body.createFixture(fixtureDef).setUserData(this);

        shape.dispose();

    }

    @Override
    public Supplier<? extends Vector2> getGeneratePosition() {
        return null;
    }

}
