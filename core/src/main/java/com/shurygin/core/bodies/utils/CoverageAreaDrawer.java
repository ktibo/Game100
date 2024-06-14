package com.shurygin.core.bodies.utils;

import com.badlogic.gdx.graphics.Color;
import com.shurygin.core.GameController;
import com.shurygin.core.LevelScreen;
import space.earlygrey.shapedrawer.JoinType;

public class CoverageAreaDrawer {

    public static final CoverageAreaDrawer EMPTY = new CoverageAreaDrawer(0f);
    private float coverage;
    private Color color;
    private float lineWidth = 0.1f;

    public CoverageAreaDrawer(float coverage) {
        this(coverage, new Color(0, 0, 0, 0.25f));
    }

    public CoverageAreaDrawer(float coverage, Color color) {
        this.coverage = coverage;
        this.color = color;
    }

    public void render(float x, float y) {
        if (GameController.debug && this != EMPTY) {
            LevelScreen.shapeDrawer.setColor(color);
            LevelScreen.shapeDrawer.circle(x, y, coverage, lineWidth, JoinType.POINTY);
        }
    }


}
