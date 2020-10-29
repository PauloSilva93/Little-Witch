package me.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {

	static int [] resolution = {800, 600};
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Little Witch in Magic Forest";
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.width = resolution[0];
		config.height = resolution[1];
		new LwjglApplication(new Game(), config);
	}
}
