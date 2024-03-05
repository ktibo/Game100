package com.shurygin.core.screens;

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
import com.shurygin.core.cards.BigCard;
import com.shurygin.core.cards.Card;
import com.shurygin.core.GameController;
import com.shurygin.core.TextLabel;
import com.shurygin.core.Texts;
import com.shurygin.core.modifiers.Modifier;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen implements Screen {

    //final Game100 game;
    private static MenuScreen instance;

    //private final OrthographicCamera camera;
    private final Viewport viewport;
    private final GameController game = GameController.getInstance();
    private final Stage stage;
    private Image shading;
    private Table table;
    private Table tableBigCard;
    private BigCard bigCard;
    private List<Card> cards;

    public static MenuScreen getInstance() {
        if (instance == null) instance = new MenuScreen();
        return instance;
    }

    private MenuScreen() {

        viewport = game.getViewport();

        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        shading = new Image(new Texture(Gdx.files.internal("background.png")));
        //shading.setTouchable(Touchable.disabled);
        shading.setColor(0, 0, 0, 0.8f);

        bigCard = new BigCard();

    }

    private void createStage() {

        stage.clear();

        shading.setVisible(false);

        createTable();

        createTableBigCard();

        Stack stack = new Stack();
        stack.setFillParent(true);
        stack.add(table);
        stack.add(shading);
        stack.add(tableBigCard);

        stage.addActor(stack);

    }

    public void cardSelected(Card card, boolean isNewCard) {
        bigCard.setModifier(card.getModifier(), isNewCard);
        setBigCardVisible(true);
    }

    private void setBigCardVisible(boolean visible) {
        shading.setVisible(visible);
        tableBigCard.setVisible(visible);
    }

    public void bigCardSelected(Modifier modifier, boolean isNewCard) {
        if (isNewCard) {
            game.addModifier(modifier);
            game.startNewLevel();
        } else {
            setBigCardVisible(false);
        }
    }

    public void startNewGame() {
        fillCards();
    }

    private void fillCards() {

        int n = Modifier.getAllModifiers().size();
        cards = new ArrayList<>(n);
        List<Modifier> allModifiers = Modifier.getAllModifiers();

        for (int i = 0; i < n; i++) {
            cards.add(new Card(allModifiers.get(i)));
        }

    }

    private void createTable() {

        table = new Table();
        //table.setBounds(0f,0f,10f,10f);
        //table.setFillParent(true);
        table.setRound(false);
        //table.pad(0.2f);
        table.defaults().width(Card.WIDTH).height(Card.HEIGHT).space(Card.WIDTH / 10f);

        int columns = 20; // how many cards in one row

        Widget label = new TextLabel(Texts.get("cardsTitle"), 20f, Color.WHITE, Align.bottom);

        table.add(label).colspan(columns);
        //table.add(new  );
        table.row();

        // Cards

        for (int i = 0; i < cards.size(); i++) {
            if (i % columns == 0) table.row();
            table.add(cards.get(i));
        }
        table.row();
        table.add();
    }

    private void createTableBigCard() {

        tableBigCard = new Table();

        tableBigCard.setRound(false);
        tableBigCard.setDebug(GameController.debug);
        tableBigCard.setVisible(false);
        float scale = 4f;
        tableBigCard.defaults().width(scale * 1f).height(scale * 1.53f);

        tableBigCard.add(bigCard);

    }

    @Override
    public void show() {
        //System.out.println("show");
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