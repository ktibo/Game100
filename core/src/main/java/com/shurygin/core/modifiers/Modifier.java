package com.shurygin.core.modifiers;

import com.shurygin.core.utils.Texts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Modifier {

    public static List<Modifier> getAllModifiers() {

        List<Modifier> allModifiers = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            allModifiers.add(new Diabetes());
            allModifiers.add(new Viruses());
            allModifiers.add(new Rabies());
            allModifiers.add(new Traffic());
        }

        Collections.shuffle(allModifiers);
        return allModifiers;

    }

    protected int amount = 0;

    public final void increaseAmount() {
        amount++;
    }

    public abstract void initialize();

    public void start() {

    }

    public void update() {

    }

    public void stop() {

    }

    public final String getTitle() {
        String key = getClass().getSimpleName() + "_title";
        return Texts.get(key);
    }

    public final String getDescription() {
        String key = getClass().getSimpleName() + "_description";
        return Texts.get(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modifier)) return false;

        Modifier modifier = (Modifier) o;

        return Objects.equals(this.getClass(), modifier.getClass());
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }

}
