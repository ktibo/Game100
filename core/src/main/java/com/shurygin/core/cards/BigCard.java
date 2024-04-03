package com.shurygin.core.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.shurygin.core.AnimationController;
import com.shurygin.core.CursorController;
import com.shurygin.core.GameController;
import com.shurygin.core.TextLabel;
import com.shurygin.core.modifiers.Modifier;
import com.shurygin.core.screens.MenuScreen;

public class BigCard extends Widget {

    private static Texture textureBackground;

    private static final int FRAME_COLS = 3;
    private static final int FRAME_ROWS = 2;

    {
        textureBackground = new Texture(Gdx.files.internal("card.png"));
        //idleAnimation = new AnimationController(textureBackground, FRAME_COLS, FRAME_ROWS, 0, 3);
        idleOpenAnimation = new AnimationController(textureBackground, FRAME_COLS, FRAME_ROWS, 1, 2);
    }

    private Stack stack;
    private Image imageBackground = new Image();
    private Image imageLogo = new Image();

    //private AnimationController idleAnimation;
    private AnimationController idleOpenAnimation;
    //private AnimationController currentAnimation;
    private AnimationController logoAnimation;

    private Modifier modifier;
    private boolean isNewCard;
    private TextLabel title;
    private TextLabel description;
    private Table table;

    public Modifier getModifier() {
        return modifier;
    }

    public BigCard() {

        stack = new Stack();

        title = new TextLabel("", 20f, new Color(Color.BLACK), Align.center);
        description = new TextLabel("", 40f, new Color(Color.BLACK), Align.topLeft);
        table = new Table();

        addListener(new Listener());

        setModifier(Modifier.getAllModifiers().get(0), true); // default

        createTable();

        stack.add(imageBackground);
        stack.add(imageLogo);
        stack.add(table);

    }

    private void createTable() {

        table.setRound(false);
        //table.defaults().width(1f).height(1.53f);
        table.setDebug(GameController.debug);

        table.add(title);
        table.row();
        table.add(description);

    }

    public void setModifier(Modifier modifier, boolean isNewCard) {
        this.modifier = modifier;
        this.isNewCard = isNewCard;
        logoAnimation = modifier.getLogoAnimation();
        title.setText(modifier.getTitle());
        description.setText(modifier.getDescription());
        //title.setPosition(table.getX(), 0f);
    }

    private void selected() {
        MenuScreen.getInstance().bigCardSelected(modifier, isNewCard);
    }

    class Listener implements EventListener {

        @Override
        public boolean handle(Event event) {

            InputEvent inputEvent = (InputEvent) event;

            CursorController.handle(inputEvent);

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
        //stack.validate();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for (Actor actor:stack.getChildren()) {
            actor.draw(batch, parentAlpha);
        }
    }
}
