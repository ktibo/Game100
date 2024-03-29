package com.shurygin.core.bodies;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.WorldManifold;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shurygin.core.AnimationController;
import com.shurygin.core.GameController;
import com.shurygin.core.screens.GameScreen;

public abstract class AbstractObject {

    protected GameScreen gameScreen;
    protected World world;
    protected Viewport viewport;
    protected Batch batch;

    protected float width;
    protected float height;

    public boolean isNeedRemove() {
        return needRemove;
    }

    protected boolean needRemove;

    public void setNeedRemove(boolean needRemove) {
        this.needRemove = needRemove;
    }

    protected AnimationController currentAnimation;
    protected Body body;
    protected ObjectType objectType;

    public ObjectType getType() {
        return objectType;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public Body getBody() {
        return body;
    }

    protected AbstractObject(AnimationController animationController, ObjectType objectType, float size) {
        this(animationController, objectType, size, size);
    }

    protected AbstractObject(AnimationController animationController, ObjectType objectType, float width, float height) {

        gameScreen = GameScreen.getInstance();
        world = gameScreen.getWorld();
        viewport = GameController.getInstance().getViewport();
        batch = gameScreen.getBatch();

        this.currentAnimation = animationController;
        this.objectType = objectType;
        this.width = width;
        this.height = height;
        gameScreen.getBodies().add(this);

    }

    public Vector2 getPosition() {

        if (body == null) {
            throw new NullPointerException("The body wasn't initialized yet!");
        }

        return body.getPosition();
    }

    public void render(float delta) {

        if (currentAnimation == null) return;
        TextureRegion currentFrame = currentAnimation.getFrame(delta);
        batch.draw(
                currentFrame,
                body.getPosition().x - width / 2,
                body.getPosition().y - height / 2,
                width / 2,
                height / 2,
                width,
                height,
                1f,
                1f,
                MathUtils.radiansToDegrees * body.getAngle()
        );

    }

    public void remove() {
        body.setActive(false);
        world.destroyBody(body);
    }

    public void touch(WorldManifold worldManifold, ObjectType type, AbstractObject object) {

    }

    public void update() {

    }

    public int getDepth() {
        return objectType.getDepth();
    }

}
