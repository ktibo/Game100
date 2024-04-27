package com.shurygin.core.modifiers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.shurygin.core.utils.AnimationController;
import com.shurygin.core.bodies.*;

import java.util.ArrayList;
import java.util.List;

public class Viruses extends Modifier {

    public static final String PATH_LOGO = "enemies/virus.png";
    private List<Virus> viruses;
    private int n;

    public Viruses() {
        super();
        viruses = new ArrayList<>();
        n = 6;
    }

    @Override
    public void initialize() {
        for (int i = 0; i < n * amount; i++) {
            viruses.add(new Virus());
        }
    }

    @Override
    public void start() {
        viruses.forEach(Virus::start);
    }

}
