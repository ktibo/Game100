package com.shurygin.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.MissingResourceException;

public abstract class Texts {

    private static I18NBundle bundle;

    static {
        //Locale locale = new Locale("en");
        bundle = I18NBundle.createBundle(Gdx.files.internal("texts"));
    }

    public static String get(String key, Object... args) {

        String value;
        try {
            value = bundle.format(key, args);
        } catch (MissingResourceException e) {
            value = "???";
        }


        return value;

    }

}
