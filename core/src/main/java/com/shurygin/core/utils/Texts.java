package com.shurygin.core.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

public abstract class Texts {

    private static I18NBundle bundle;

    static {
        //Locale locale = new Locale("en");
        bundle = I18NBundle.createBundle(Gdx.files.internal("texts"));
    }

    public static String get(String key, Object... args) {
        return bundle.format(key, args);
    }

}

