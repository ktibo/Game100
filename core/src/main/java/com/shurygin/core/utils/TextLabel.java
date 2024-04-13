package com.shurygin.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.shurygin.core.GameController;

public class TextLabel extends Widget {
    private Label label;
    private OrthographicCamera camera;
    private Matrix4 matrix;
    private float scale;
    private float offsetX;
    Label.LabelStyle labelStyle;
    private int alignment;

    public TextLabel(CharSequence text, float scale, Color color, int alignment) {

        this.scale = scale;
        this.alignment = alignment;
        offsetX = -1 * scale / 15f;

        camera = new OrthographicCamera();
        //camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, GameController.WIDTH * scale, GameController.HEIGHT * scale);
        matrix = camera.combined;

        labelStyle = new Label.LabelStyle();
        BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
        //font.setUseIntegerPositions(false);
        labelStyle.font = font;
        labelStyle.fontColor = color;
        //label = new Label(text, labelStyle);
        setText(text);

    }

    public void setText(CharSequence text) {
        label = new Label(text, labelStyle);
        label.setAlignment(alignment);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.setProjectionMatrix(matrix);

        label.setSize(getWidth(), getHeight());
        label.setPosition((getX() + getWidth() / 2f) * scale + offsetX, (getY() + getHeight() / 2f) * scale);
        label.draw(batch, parentAlpha);

        batch.setProjectionMatrix(GameController.getInstance().getCamera().combined);

    }
}
