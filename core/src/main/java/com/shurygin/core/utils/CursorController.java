package com.shurygin.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class CursorController {

    // Solving the problem:
    // releasing mouse button over an object considered as exit event
    // To prevent the cursor turning on to the arrow the "entered" was added

    private static boolean entered = false;

    public static void handle(InputEvent event) {

        InputEvent.Type type = event.getType();

        if (type == InputEvent.Type.exit && !entered) {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Arrow);
        } else {
            Gdx.graphics.setSystemCursor(Cursor.SystemCursor.Hand);
        }

        entered = (type == InputEvent.Type.enter);

    }


}
