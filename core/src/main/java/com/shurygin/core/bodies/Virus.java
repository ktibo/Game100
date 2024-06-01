package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.GameController;
import com.shurygin.core.modifiers.Viruses;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.ContactListenerClass;

public class Virus extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("enemies/virus.png"));
    private static float speed = 2f;
    private static float size = 1.5f * GameController.SIZE;
    private final Viruses viruses;

    public Virus(Viruses viruses) {

        super(new AnimationController(texture), ObjectType.ENEMY, size);
        this.viruses = viruses;
        bodyController.generatePosition(this);

    }

    @Override
    protected BodyDef createBodyDef() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        bodyDef.linearDamping = 0f;
        return bodyDef;
    }

    @Override
    protected Shape createShape() {
        CircleShape shape = new CircleShape();
        shape.setRadius(width / 2 * 0.85f);
        return shape;
    }

    @Override
    protected FixtureDef createFixtureDef() {
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 10f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 1f;
        fixtureDef.filter.categoryBits = FilterCategory.SOLID;
        fixtureDef.filter.maskBits = (short) (FilterCategory.WALL | FilterCategory.PLAYER | FilterCategory.SOLID);
        return fixtureDef;
    }

    public void start() {
        Vector2 v = Vector2.X.setLength(speed).setAngleDeg(MathUtils.random(360f));
        body.setLinearVelocity(v);
        body.setAngularVelocity(MathUtils.random(-1f, 1f));
    }

    @Override
    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {

        if (type == ObjectType.PLAYER && !viruses.playerHasImmunity())
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
    public Vector3 getStartTransform() {
        return getRandomTransform(true);
    }

}
