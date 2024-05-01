package com.shurygin.core;

import java.util.concurrent.CyclicBarrier;

public class GameTest extends GameController {

    public static CyclicBarrier barrier = new CyclicBarrier(2);

    @Override
    public void create() {
        super.create();
        try {
            barrier.await();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void test(Runnable runnable){
        runnable.run();
    }

}
