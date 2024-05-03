package com.shurygin.core.bodies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.function.Supplier;

public class BorderBody extends AbstractBody {

    private Vector2 position;

    public BorderBody(float size, Vector2 position) {
        super(null, ObjectType.WALL, size);
        this.position = position;
        bodyController.generatePosition(this);
    }

    @Override
    protected BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        return bodyDef;
    }

    @Override
    protected Shape createShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(width);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.filter.categoryBits = FilterCategory.WALL;
        fixtureDef.filter.maskBits = (short) (FilterCategory.SOLID | FilterCategory.SENSOR);
        return fixtureDef;
    }

    @Override
    public Vector3 getTransform() {
        Vector3 startPosition = new Vector3();
        startPosition.x = position.x;
        startPosition.y = position.y;
        startPosition.z = 0f;
        return startPosition;
    }

}
