package com.shurygin.core.modifiers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.shurygin.core.bodies.*;
import com.shurygin.core.utils.AnimationController;

import java.util.ArrayList;
import java.util.List;

public class Rabies extends Modifier {

    public static final String PATH_LOGO = "enemies/animal.png";
    private List<Animal> animals;
    private int n;

    public Rabies() {
        super();
        animals = new ArrayList<>();
        n = 3;
    }

    @Override
    public void initialize() {
        for (int i = 0; i < n * amount; i++) {
            animals.add(new Animal());
        }
    }

    @Override
    public void start() {
        animals.forEach(Animal::start);
    }

}
