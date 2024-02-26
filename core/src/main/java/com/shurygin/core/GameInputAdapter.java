package com.shurygin.core;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameInputAdapter extends InputAdapter {

    private static GameInputAdapter instance;
    private Viewport viewport;
    private Vector2 mousePosition = new Vector2();

    public static GameInputAdapter getInstance() {
        if (instance == null) {
            instance = new GameInputAdapter();
        }
        return instance;
    }

    private GameInputAdapter() {
        viewport = GameController.getInstance().getViewport();
        //player = Player.getInstance();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        //return super.touchDown(screenX, screenY, pointer, button);
//        mousePosition.x = screenX;
//        mousePosition.y = screenY;
//        viewport.unproject(mousePosition);
        //player.move(mousePosition);

        //System.out.println("screenX: "+screenX);


        return true;
    }
}
