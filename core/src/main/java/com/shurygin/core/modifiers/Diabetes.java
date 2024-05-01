package com.shurygin.core.modifiers;

import com.shurygin.core.bodies.Syringe;

public class Diabetes extends Modifier {

    public static final String PATH_LOGO = "syringe.png";

    public Diabetes() {
        super();
    }

    @Override
    public void initialize() {
        for (int i = 0; i < amount; i++) {
            new Syringe();
        }
    }

}
