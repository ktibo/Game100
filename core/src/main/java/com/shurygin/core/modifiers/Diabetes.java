package com.shurygin.core.modifiers;

import com.shurygin.core.bodies.BodyController;
import com.shurygin.core.bodies.Syringe;

public class Diabetes extends Modifier {

    public static final String PATH_LOGO = "syringe.png";
    private boolean immunity;
    private int activations;

    public Diabetes() {
        super();
    }

    @Override
    public void initialize() {
        immunity = false;
        activations = 0;
        for (int i = 0; i < amount; i++) {
            new Syringe(this);
            activations++;
        }
        if (activations > 0) BodyController.getInstance().getTarget().setActive(false);
    }

    public boolean isImmunity() {
        return immunity;
    }

    public void setImmunity(boolean immunity) {
        this.immunity = immunity;
    }

    public void subtractActivation() {
        if (--activations == 0) BodyController.getInstance().getTarget().setActive(true);
    }
}
