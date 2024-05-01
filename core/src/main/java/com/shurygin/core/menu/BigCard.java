package com.shurygin.core.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shurygin.core.GameController;
import com.shurygin.core.modifiers.Modifier;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.TextLabel;

public class BigCard extends Widget implements MouseHanded{

    private static Texture textureBackground;

    public static final float SCALE = 4.0f;
    private static final int FRAME_COLS = 3;
    private static final int FRAME_ROWS = 2;

    private final MenuController menuController;

    {
        textureBackground = new Texture(Gdx.files.internal("card.png"));
        idleOpenAnimation = new AnimationController(textureBackground, FRAME_COLS, FRAME_ROWS, 1, 2);
    }

    private Stack stack;
    private Image imageBackground = new Image();
    private Image imageLogo = new Image();

    private AnimationController idleOpenAnimation;
    private AnimationController logoAnimation = AnimationController.getLogoAnimationController(Modifier.getAllModifiers().get(0)); // default

    private TextLabel title = new TextLabel("", 20f, new Color(Color.BLACK), Align.center);;
    private TextLabel description = new TextLabel("", 40f, new Color(Color.BLACK), Align.topLeft);
    private Table table = new Table();;

    public BigCard(MenuController menuController) {

        this.menuController = menuController;
        stack = new Stack();

        addListener(new Listener());

        createTable();

        stack.add(imageBackground);
        stack.add(imageLogo);
        stack.add(table);

    }

    private void createTable() {

        table.setRound(false);

        table.setDebug(GameController.debug);

        table.add(title);
        table.row();
        table.add(description);

    }

    public void setModifier() {

        Modifier modifier = menuController.getModifier();

        logoAnimation = AnimationController.getLogoAnimationController(modifier);
        title.setText(modifier.getTitle());
        description.setText(modifier.getDescription());

    }

    private void selected() {
        menuController.bigCardSelected();
    }

    @Override
    public boolean mouseHand() {
        return true;
    }

    class Listener implements EventListener {

        @Override
        public boolean handle(Event event) {

            InputEvent inputEvent = (InputEvent) event;

            if (inputEvent.getType() == InputEvent.Type.touchDown && inputEvent.getButton() == 0) {
                selected();
            }

            return true;

        }
    }

    @Override
    public void act(float delta) {

        imageBackground.setDrawable(new TextureRegionDrawable(idleOpenAnimation.getFrame(delta)));
        imageBackground.setSize(getWidth(), getHeight());
        imageBackground.setPosition(getX(), getY());

        float logoScale = 0.4f;
        float logoOffsetY = 0.9f;

        TextureRegion logoFrame = logoAnimation.getFrame(delta);
        imageLogo.setDrawable(new TextureRegionDrawable(logoFrame));
        float logoRatio = (float) logoFrame.getRegionHeight() / logoFrame.getRegionWidth();
        imageLogo.setSize(getWidth() * logoScale, getWidth() * logoScale*logoRatio);
        imageLogo.setPosition(getX() + getWidth() / 2f - imageLogo.getWidth() / 2f,
                getY() + getHeight() / 2f - imageLogo.getHeight()*logoRatio / 2f + logoOffsetY);


        float titleOffsetY = -0.5f;
        title.setPosition(getX() + getWidth() / 2f, getY() + getHeight() + titleOffsetY);

        float descriptionOffsetX = 0.3f;
        description.setPosition(getX() + descriptionOffsetX, getY() + getHeight() / 2f);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        stack.getChildren().forEach(actor -> actor.draw(batch, parentAlpha));
    }
}
