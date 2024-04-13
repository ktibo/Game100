package com.shurygin.core.utils;

import com.badlogic.gdx.math.Vector2;
import com.shurygin.core.GameController;
import com.shurygin.core.bodies.Wall;

public class BorderController {

    //private static List<AbstractObject> walls;
    private static float thickness;

    public static float getThickness(){
        return thickness;
    }
    public static float getLength(){
        return Math.max(GameController.HEIGHT, GameController.WIDTH);
    }

    public static void setBorders(){

        thickness = 0.5f;
        //walls = new ArrayList<>();

        // left
        new Wall(new Vector2(thickness / 2f, GameController.HEIGHT / 2f), 0f);
        // right
        new Wall(new Vector2(GameController.WIDTH - thickness / 2f, GameController.HEIGHT / 2f), 180f);
        // up
        new Wall(new Vector2(GameController.WIDTH / 2f, GameController.HEIGHT - thickness / 2f), 270f);
        // down
        new Wall(new Vector2(GameController.WIDTH / 2f, thickness / 2f), 90f);

    }

//    public static void draw(float delta){
//        for (AbstractObject object : walls) {
//            object.render(delta);
//        }
//    }

    //public static List<AbstractObject> getWalls() {
    //  return walls;
    //}

}
