package com.shurygin.core.modifiers;

import com.shurygin.core.bodies.Cigarette;

public class Tobacco extends Modifier {

    public static final String PATH_LOGO = "tobacco.png";

    public Tobacco() {
        super();
    }

    @Override
    public void initialize() {
        for (int i = 0; i < amount; i++) {
            new Cigarette();
        }
    }

}
