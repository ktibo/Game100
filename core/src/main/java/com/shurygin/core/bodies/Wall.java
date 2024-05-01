package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.BorderController;

import java.util.function.Supplier;

public class Wall extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("wall.png"));

    public Wall(Vector2 position, float angle) {

        super(new AnimationController(texture),
                ObjectType.WALL,
                BorderController.getThickness(),
                BorderController.getLength());

        body.setTransform(position, angle / MathUtils.radiansToDegrees);

    }

    @Override
    protected BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        return bodyDef;
    }

    @Override
    protected Shape createShape() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(BorderController.getThickness() / 2 - 0.04f, height / 2);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = FilterCategory.WALL;
        fixtureDef.filter.maskBits = (short) (FilterCategory.PLAYER | FilterCategory.SOLID);
        return fixtureDef;
    }

    @Override
    public Supplier<? extends Vector3> getGeneratePosition() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean avoidCollisionsAtBeginning() {
        return false;
    }

}
