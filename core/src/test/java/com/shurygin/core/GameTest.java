package com.shurygin.core;

public class GameTest extends GameController {

    public static volatile Boolean appStarted;

    @Override
    public void create() {
        super.create();
        appStarted = true;
    }

    public void test(Runnable runnable){
        runnable.run();
    }

}
