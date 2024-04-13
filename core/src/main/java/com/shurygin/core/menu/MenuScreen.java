package com.shurygin.core.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shurygin.core.GameController;
import com.shurygin.core.utils.TextLabel;
import com.shurygin.core.utils.Texts;

public class MenuScreen implements Screen {

    public static final int COLUMNS = 10; // how many cards in one row

    private final MenuController menuController;
    private final Viewport viewport;
    private final Stage stage;

    private Image shading;
    private Table table;
    private Table tableBigCard;
    private BigCard bigCard;

    public MenuScreen(MenuController menuController) {

        this.menuController = menuController;
        viewport = GameController.getInstance().getViewport();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        bigCard = new BigCard(menuController);

    }

    private void createStage() {

        stage.clear();

        shading = new Image(new Texture(Gdx.files.internal("cardShading.png")));
        shading.setColor(0, 0, 0, 0.8f);
        shading.setVisible(false);

        createTable();
        createBigCard();

        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(table);
        stack.add(shading);
        stack.add(tableBigCard);

        stage.addActor(stack);

    }

    public void cardSelected() {
        bigCard.setModifier();
        setBigCardVisible(true);
    }

    private void createTable() {

        table = new Table();
        table.setRound(false);
        table.defaults().width(Card.WIDTH).height(Card.HEIGHT).space(Card.WIDTH / 10f);

        Widget label = new TextLabel(Texts.get("cardsTitle"), 20f, Color.WHITE, Align.bottom);

        table.add(label).colspan(COLUMNS);
        table.row();

        // Cards

        for (int i = 0; i < menuController.cardCount(); i++) {
            if (i % COLUMNS == 0) table.row();
            table.add(menuController.getCard(i));
        }
        table.row();
        table.add();
    }

    private void createBigCard() {

        tableBigCard = new Table();

        tableBigCard.setRound(false);
        tableBigCard.setVisible(false);
        tableBigCard.defaults().width(BigCard.SCALE * Card.WIDTH).height(BigCard.SCALE * Card.HEIGHT);

        tableBigCard.add(bigCard);

    }

    public void closeBigCard() {
        setBigCardVisible(false);
    }

    private void setBigCardVisible(boolean visible) {
        shading.setVisible(visible);
        tableBigCard.setVisible(visible);
    }

    @Override
    public void show() {
        createStage();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        stage.clear();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}