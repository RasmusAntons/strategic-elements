package net.frozenbit.strategicelements;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.frozenbit.strategicelements.screens.LoginScreen;
import net.frozenbit.strategicelements.screens.MainMenu;
import net.frozenbit.strategicelements.screens.ScreenManager;

public class StrategicElementsGame extends ApplicationAdapter {
	ScreenManager screenManager;
	
	@Override
	public void create() {
		screenManager = new ScreenManager();
		screenManager.push(new LoginScreen());
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
