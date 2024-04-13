package com.shurygin.core.utils;

import java.util.Observable;

public class PauseObservable extends Observable {
    @Override
    public synchronized void setChanged() {
        super.setChanged();
    }
}
