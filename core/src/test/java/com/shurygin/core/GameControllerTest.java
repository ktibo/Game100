package com.shurygin.core;

import com.shurygin.core.menu.MenuController;
import com.shurygin.core.modifiers.Modifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Spy
    private GameController gameController = new GameController();

    @Test
    void dispose() throws NoSuchFieldException, IllegalAccessException {

        // When invoke dispose() in gameController, then must be invoked dispose()
        // in menuController and levelController

        MenuController menuController = Mockito.mock(MenuController.class);
        LevelController levelController= Mockito.mock(LevelController.class);

        Class<? extends GameController> aClass = gameController.getClass();

        Field menuControllerField = aClass.getDeclaredField("menuController");
        menuControllerField.setAccessible(true);
        menuControllerField.set(gameController, menuController);

        Field levelControllerField = aClass.getDeclaredField("levelController");
        levelControllerField.setAccessible(true);
        levelControllerField.set(gameController, levelController);

        gameController.dispose();

        Mockito.verify(menuController, Mockito.times(1)).dispose();
        Mockito.verify(levelController, Mockito.times(1)).dispose();

    }

    @Test
    void addModifier() throws ReflectiveOperationException {

        // Modifiers count equals by their classes

        Set<Modifier> modifiersSet = new HashSet<>(Modifier.getAllModifiers());

        Iterator<Modifier> iterator = modifiersSet.iterator();

        Modifier modifier1 = iterator.next();
        Modifier modifier2 = iterator.next();
        Modifier modifier2_2 = modifier2.getClass().getConstructor().newInstance(); // same class

        gameController.addModifier(modifier1);
        gameController.addModifier(modifier2);
        gameController.addModifier(modifier2_2);

        Assertions.assertEquals(2, gameController.getModifiers().size());

    }

}