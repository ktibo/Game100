package com.shurygin.core.bodies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.shurygin.core.GameController;
import com.shurygin.core.utils.AnimationController;

public abstract class AbstractBody implements Comparable {

    protected BodyController bodyController;
    protected float width;
    protected float height;
    protected Body body;
    protected ObjectType objectType;
    protected RenderObject renderObject;
    protected Player player;
    protected boolean avoidCollisions = false;
    private boolean needDestroy;

    protected AbstractBody(AnimationController animationController, ObjectType objectType, float size) {
        this(animationController, objectType, size, size);
    }

    protected AbstractBody(AnimationController animationController, ObjectType objectType, float width, float height) {

        bodyController = BodyController.getInstance();
        this.renderObject = new RenderObject(this, animationController);
        this.objectType = objectType;
        this.width = width;
        this.height = height;

        bodyController.addBody(this);
        player = bodyController.getPlayer();

        body = bodyController.createBody(createBodyDef());
        FixtureDef fixtureDef = createFixtureDef();
        Shape shape = createShape();
        fixtureDef.shape = shape;
        body.createFixture(fixtureDef).setUserData(this);
        shape.dispose();
        //bodyController.generatePosition(this);

    }

    protected Vector3 getRandomTransform(boolean randomAngle) {

        float wallThickness = Wall.getThickness();
        float width = getWidth();
        float height = getHeight();
        Vector3 pos = new Vector3();

        pos.x = MathUtils.random(wallThickness + width / 2, GameController.WIDTH - wallThickness - width / 2);
        pos.y = MathUtils.random(wallThickness + height / 2, GameController.HEIGHT - wallThickness - height / 2);
        pos.z = randomAngle ? MathUtils.PI2 * MathUtils.random() : 0f;

        return pos;

    }

    public Vector2 getPosition() {

        if (body == null) {
            throw new NullPointerException("The body wasn't initialized yet!");
        }
        return body.getPosition();
    }

    protected abstract BodyDef createBodyDef();
    protected abstract Shape createShape();
    protected abstract FixtureDef createFixtureDef();

    public void render(float delta){
        renderObject.render(delta);
    }

    public void destroy() {
        body.setActive(false);
    }

    public void touch(WorldManifold worldManifold, ObjectType type, AbstractBody object) {

    }

    public void update() {

    }

    public int getDepth() {
        return objectType.getDepth();
    }

    public boolean isNeedDestroy() {
        return needDestroy;
    }

    public void setNeedDestroy(boolean needDestroy) {
        this.needDestroy = needDestroy;
    }

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

    public float getAngle() {
        return body.getAngle();
    }

    @Override
    public int compareTo(Object o) {
        AbstractBody body = (AbstractBody) o;
        return getDepth() == body.getDepth() ? Integer.compare(hashCode(), body.hashCode()) : Integer.compare(getDepth(), body.getDepth());
    }

    public Vector3 getStartTransform() {
        throw new UnsupportedOperationException();
    }

    public boolean avoidCollisionsAtBeginning() {
        return true;
    }

}
