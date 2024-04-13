package com.shurygin.core;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.shurygin.core.utils.Texts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.MissingResourceException;

import static org.junit.jupiter.api.Assertions.*;

class TextsTest {

    private static GameTest game;
    private static Thread gameThread;

    @BeforeAll
    static void createApp(){
        GameTest.appStarted = false;
        game = new GameTest();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

        gameThread = new Thread(() -> new Lwjgl3Application(game, config));
        //thread.setDaemon(true);
        gameThread.start();

        while (!GameTest.appStarted){
            // wait until our test application started
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

    @ParameterizedTest
    @DisplayName("Texts test")
    @CsvSource({"'тест 123', 'test'"})
    void get(String expected, String key) {
        game.test(() -> {
            assertAll(
                    ()->assertEquals(expected, Texts.get(key)),
                    ()->assertThrows(MissingResourceException.class, ()->Texts.get("test exception")));
        });

    }
}