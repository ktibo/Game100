package com.shurygin.java;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.shurygin.core.GameController;

public class GameControllerDesktop {
	public static void main (String[] args) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		config.setTitle("Game100");
		config.setWindowedMode(1024, 768);
		config.useVsync(false);
		config.setForegroundFPS(0);

		new Lwjgl3Application(GameController.getInstance(), config);
	}
}
