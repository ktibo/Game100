package com.shurygin.core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.shurygin.core.bodies.utils.ObjectType;
import com.shurygin.core.utils.Texts;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.Comparator;
import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.*;

class Test {

    private static GameTest game;
    private static Thread gameThread;

    @ParameterizedTest
    @DisplayName("Texts test")
    @CsvSource({"'тест 123', 'test'"})
    void getText(String expected, String key) {
        game.test(() -> {
            assertAll(
                    ()->assertEquals(expected, Texts.get(key)),
                    ()->assertThrows(MissingResourceException.class, ()->Texts.get("test exception")));
        });

    }

    @org.junit.jupiter.api.Test
    @DisplayName("ObjectType test")
    void objectTypeTest(){

        // COLLECTABLE must be the lowest, WALL must be the highest in depth hierarchy

        Object[] array = Arrays.stream(ObjectType.values()).
                sorted(Comparator.comparingInt(ObjectType::getDepth)).
                toArray();

        assertSame(ObjectType.COLLECTABLE, array[0]);
        assertSame(ObjectType.WALL, array[array.length-1]);

    }


    @BeforeAll
    static void createApp() {

        game = new GameTest();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        gameThread = new Thread(() -> new Lwjgl3Application(game, config));
        gameThread.start();

        // wait until our test application started
        try {
            GameTest.barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @AfterAll
    static void closeApp(){
        game.exit();
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}