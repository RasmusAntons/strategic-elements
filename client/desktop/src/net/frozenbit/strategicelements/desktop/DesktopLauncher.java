package net.frozenbit.strategicelements.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.frozenbit.strategicelements.StrategicElementsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 700;
		new LwjglApplication(new StrategicElementsGame(), config);
	}
}
