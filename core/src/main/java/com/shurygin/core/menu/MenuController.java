package com.shurygin.core.menu;

import com.shurygin.core.GameController;
import com.shurygin.core.modifiers.Modifier;

import java.util.ArrayList;

public class MenuController {

    private GameController game;
    private MenuScreen menuScreen;
    private ArrayList<Card> cards;
    private Modifier modifier;
    private boolean isNewCard;

    public void cardSelected(Card card, boolean isNewCard) {
        modifier = card.getModifier();
        this.isNewCard = isNewCard;
        menuScreen.cardSelected();
    }

    public void bigCardSelected() {
        if (isNewCard) {
            game.addModifier(modifier);
            game.startNewLevel();
        } else {
            menuScreen.closeBigCard();
        }
    }

    public Modifier getModifier() {
        return modifier;
    }
    public int cardCount(){
        return cards.size();
    }
    public Card getCard(int i) {
        return cards.get(i);
    }
    public void showMenu() {
        GameController.changeScreen(menuScreen);
    }
    public void dispose() {
        menuScreen.dispose();
    }

    public MenuController(GameController gameController) {
        game = gameController;
        menuScreen = new MenuScreen(this);
        cards = Card.getCardDeck(this);
    }

}
