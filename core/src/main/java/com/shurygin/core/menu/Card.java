package com.shurygin.core.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.utils.CursorController;
import com.shurygin.core.GameController;
import com.shurygin.core.modifiers.Modifier;

import java.util.ArrayList;

public class Card extends Widget {

    public static final float WIDTH = 0.8f;
    public static final float HEIGHT = WIDTH * 1.53f;

    private static Texture textureBackground = new Texture(Gdx.files.internal("card.png"));

    private static final int FRAME_COLS = 3;
    private static final int FRAME_ROWS = 2;
    private static final float LOGO_SIZE = WIDTH * 0.7f;

    private final MenuController menuController;

    private Image imageBackground = new Image();
    private Image imageLogo = new Image();

    private AnimationController idleAnimation;
    private AnimationController idleOpenAnimation;
    private AnimationController currentAnimation;
    private AnimationController logoAnimation;

    private Modifier modifier;

    public Modifier getModifier() {
        return modifier;
    }

    private boolean isOpened;

    public Card(MenuController menuController, Modifier modifier) {

        this.menuController = menuController;
        this.modifier = modifier;
        addListener(new Listener());
        validate();
        logoAnimation = AnimationController.getLogoAnimationController(modifier);

    }

    class Listener implements EventListener {

        @Override
        public boolean handle(Event event) {

            InputEvent inputEvent = (InputEvent) event;

            if (!isOpened) {
                CursorController.handle(inputEvent);
            }

            if (inputEvent.getType() == InputEvent.Type.touchDown) {
                int button = inputEvent.getButton();
                if (!(button == 0 && !isOpened || button == 1 && isOpened)) {
                    return false;
                }
                menuController.cardSelected(Card.this, !isOpened);
                isOpened = true;
                currentAnimation = idleOpenAnimation;
            }

            return true;

        }
    }

    @Override
    public void act(float delta) {

        if (idleAnimation == null) {
            idleAnimation = new AnimationController(textureBackground, FRAME_COLS, FRAME_ROWS, 0, 3);
            currentAnimation = idleAnimation;
        }
        if (idleOpenAnimation == null) {
            idleOpenAnimation = new AnimationController(textureBackground, FRAME_COLS, FRAME_ROWS, 1, 2);
        }

        imageBackground.setDrawable(new TextureRegionDrawable(currentAnimation.getFrame(delta)));
        imageBackground.setSize(getWidth(), getHeight());
        imageBackground.setPosition(getX(), getY());

        TextureRegion logoFrame = logoAnimation.getFrame(delta);
        imageLogo.setDrawable(new TextureRegionDrawable(logoFrame));
        float logoRatio = (float) logoFrame.getRegionHeight() / logoFrame.getRegionWidth();
        imageLogo.setSize(LOGO_SIZE, LOGO_SIZE*logoRatio);
        imageLogo.setPosition(getX() + getWidth() / 2f - LOGO_SIZE / 2f, getY() + getHeight() / 2f - LOGO_SIZE*logoRatio / 2f);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        imageBackground.draw(batch, parentAlpha);
        if (isOpened || GameController.debug) {
            imageLogo.draw(batch, parentAlpha);
        }
    }


    public static ArrayList<Card> getCardDeck(MenuController menuController){
        ArrayList<Card> cards = new ArrayList<>();
        for (Modifier modifier : Modifier.getAllModifiers()) {
            cards.add(new Card(menuController, modifier));
        }
        return cards;
    }

}
