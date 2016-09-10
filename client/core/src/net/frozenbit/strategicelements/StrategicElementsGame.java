package net.frozenbit.strategicelements;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.frozenbit.strategicelements.screens.LoginScreen;
import net.frozenbit.strategicelements.screens.MainMenu;
import net.frozenbit.strategicelements.screens.ScreenManager;

public class StrategicElementsGame extends ApplicationAdapter {
	Connection connection;
	ScreenManager screenManager;
	
	@Override
	public void create() {
		try {
			connection = new Connection("localhost", 5123);
		} catch(GdxRuntimeException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		screenManager = new ScreenManager();
		screenManager.push(new LoginScreen(connection));
	}

	@Override
	public void render() {
		screenManager.render();
	}
	
	@Override
	public void dispose() {
		screenManager.dispose();
	}
}
