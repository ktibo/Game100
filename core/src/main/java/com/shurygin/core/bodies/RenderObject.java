package com.shurygin.core.bodies;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.shurygin.core.LevelScreen;
import com.shurygin.core.utils.AnimationController;

public class RenderObject {

    protected AbstractBody object;
    protected AnimationController currentAnimation;

    public RenderObject(AbstractBody object, AnimationController animationController) {
        this.object = object;
        this.currentAnimation = animationController;
    }

    public void render(float delta) {

        if (currentAnimation == null) return;
        TextureRegion currentFrame = currentAnimation.getFrame(delta);
        LevelScreen.BATCH.draw(
                currentFrame,
                object.getPosition().x - object.getWidth() / 2,
                object.getPosition().y - object.getHeight() / 2,
                object.getWidth() / 2,
                object.getHeight()  / 2,
                object.getWidth(),
                object.getHeight(),
                1f,
                1f,
                MathUtils.radiansToDegrees * object.getAngle()
        );

    }

}
