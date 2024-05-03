package com.shurygin.core.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.shurygin.core.GameController;
import com.shurygin.core.utils.AnimationController;

public class Wall extends AbstractBody {

    private static Texture texture = new Texture(Gdx.files.internal("wall.png"));
    private static float thickness;

    public Wall(WallSide wallSide) {

        super(new AnimationController(texture),
                ObjectType.WALL,
                Wall.getThickness(),
                Wall.getLength());

        body.setTransform(wallSide.position, wallSide.angle/ MathUtils.radiansToDegrees);

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
        shape.setAsBox(Wall.getThickness() / 2 - 0.04f, height / 2);
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
    public boolean avoidCollisionsAtBeginning() {
        return false;
    }

    public static float getThickness(){
        return thickness;
    }

    public static float getLength(){
        return Math.max(GameController.HEIGHT, GameController.WIDTH);
    }

    public static void setWalls(){

        thickness = 0.5f;

        new Wall(WallSide.LEFT);
        new Wall(WallSide.RIGHT);
        new Wall(WallSide.UP);
        new Wall(WallSide.DOWN);

    }

    enum WallSide{

        LEFT(new Vector2(Wall.getThickness() / 2f, GameController.HEIGHT / 2f), 0f),
        RIGHT(new Vector2(GameController.WIDTH - Wall.getThickness() / 2f, GameController.HEIGHT / 2f), 180f),
        UP(new Vector2(GameController.WIDTH / 2f, GameController.HEIGHT - Wall.getThickness() / 2f), 270f),
        DOWN(new Vector2(GameController.WIDTH / 2f, Wall.getThickness() / 2f), 90f);

        Vector2 position;
        float angle;
        WallSide(Vector2 position, float angle) {
            this.position = position;
            this.angle = angle;
        }
    }

}