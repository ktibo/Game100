package com.shurygin.java;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import com.shurygin.core.GameController;

public class GameControllerDesktop {
	public static void main (String[] args) {
		//LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//new LwjglApplication(GameController.getInstance(), config);
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Game100");
		//config.setWindowedMode(640, 480);
		config.setWindowedMode(1000, 500);
		config.useVsync(true);
		config.setForegroundFPS(0);
		new Lwjgl3Application(GameController.getInstance(), config);
	}
}
