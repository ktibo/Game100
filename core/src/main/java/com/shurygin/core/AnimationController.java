package com.shurygin.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

public class AnimationController {

    private static final float FRAME_DURATION = 0.2f;

    private Animation<TextureRegion> animation;
    private float stateTime;

    public AnimationController(Texture texture) {
        this(texture, 3, 1);
    }

    public AnimationController(Texture texture, int FRAME_COLS, int FRAME_ROWS) {
        this(texture, FRAME_COLS, FRAME_ROWS, 0, FRAME_COLS);
    }

    public AnimationController(Texture texture, int FRAME_COLS, int FRAME_ROWS, int row, int n) {

        this(texture, FRAME_COLS, FRAME_ROWS, row, n, MathUtils.random(FRAME_DURATION * n));

    }

    public AnimationController(Texture texture, int FRAME_COLS, int FRAME_ROWS, int row, int n, float stateTime) {

        this.stateTime = stateTime;
        TextureRegion[][] tmp = TextureRegion.split(texture,
                texture.getWidth() / FRAME_COLS,
                texture.getHeight() / FRAME_ROWS);

        TextureRegion[] animationFrames = new TextureRegion[n];

        for (int i = 0; i < n; i++) {
            animationFrames[i] = tmp[row][i];
        }

        animation = new Animation(FRAME_DURATION, animationFrames);

    }

    public TextureRegion getFrame(float delta) {
        stateTime += delta;
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);
        return currentFrame;
    }
}
