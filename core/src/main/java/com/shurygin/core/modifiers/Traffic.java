package com.shurygin.core.modifiers;

import com.badlogic.gdx.utils.Timer;
import com.shurygin.core.GameController;
import com.shurygin.core.bodies.Car;

import java.util.Observer;

public class Traffic extends Modifier implements Observer {

    public static final String PATH_LOGO = "enemies/car.png";
    Timer timer;

    private float delay;

    public Traffic() {
        super();
    }

    @Override
    public void initialize() {
        timer = new Timer();
        delay = 2f / (float) amount;

        GameController.getInstance().addPauseObserver(this);

        timer.scheduleTask(new Timer.Task() {
                               @Override
                               public void run() {
                                   new Car().start();
                               }
                           }, delay, delay
        );
        timer.stop();
    }

    @Override
    public void start() {
        timer.start();
    }

    @Override
    public void stop() {
        timer.clear();
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        if (timer == null) return;
        if ((boolean)arg){
            timer.stop();
        } else {
            timer.start();
        }

    }
}
