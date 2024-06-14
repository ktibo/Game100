package com.shurygin.core.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.bodies.utils.AbstractBody;

public class ContactListenerClass implements ContactListener {

    private AbstractBody objectA;
    private AbstractBody objectB;
    private WorldManifold worldManifold;

    //private AbstractObject object;

    @Override
    public void beginContact(Contact contact) {

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        if (fixtureA == null || fixtureB == null) return;

        worldManifold = contact.getWorldManifold();

        objectA = (AbstractBody) fixtureA.getUserData();
        objectB = (AbstractBody) fixtureB.getUserData();

        objectA.touch(worldManifold, objectB.getType(), objectB);
        objectB.touch(worldManifold, objectA.getType(), objectA);

    }

    public static void handleRebound(WorldManifold worldManifold, Body body, boolean reflect) {

        Vector2 normal = worldManifold.getNormal();
        Vector2 point = worldManifold.getPoints()[0];

        Vector2 v1 = body.getLinearVelocity();
        float speed = v1.len();
        Vector2 pos = body.getPosition();
        Vector2 v2;

        normal.set(pos.x - point.x, pos.y - point.y).nor();

        if (reflect) { // "the angle of the reflected ray is equal to the angle of the incident ray"

            float dot2 = v1.dot(normal) * 2f;

            Vector2 v = normal.setLength(dot2);

            if (dot2 < 0) v.rotateDeg(180);

            v2 = v1.sub(v);

        } else { // reflects perpendicular, as the normal
            v2 = normal;
        }

        v2.setLength(speed);

        body.setLinearVelocity(v2.x, v2.y);

    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
