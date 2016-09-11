package net.frozenbit.strategicelements;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.frozenbit.strategicelements.entities.DummyEntity;
import net.frozenbit.strategicelements.screens.BoardScreen;
import net.frozenbit.strategicelements.screens.LoginScreen;
import net.frozenbit.strategicelements.screens.MainMenu;
import net.frozenbit.strategicelements.screens.ScreenManager;

public class StrategicElementsGame extends ApplicationAdapter {
	private Connection connection;
	private TextureAtlas textureAtlas;
	private GameState state;

	private ScreenManager screenManager;
	private FontManager fontManager;

	@Override
	public void create() {
		initState();
		screenManager.push(new LoginScreen(this));
	}

	private void initState() {
		state = new GameState();
		try {
			connection = new Connection("raspi.rasmusantons.de", 5123);
		} catch (GdxRuntimeException e) {
			e.printStackTrace();
			//System.exit(-1);
		}
		textureAtlas = new TextureAtlas(Gdx.files.internal("textures.atlas"));
		screenManager = new ScreenManager();
		fontManager = new FontManager();
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public Connection getConnection() {
		return connection;
	}

	public ScreenManager getScreenManager() {
		return screenManager;
	}

	public GameState getState() {
		return state;
	}

	public FontManager getFontManager() {
		return fontManager;
	}

	@Override
	public void render() {
		screenManager.render();
	}

	@Override
	public void dispose() {
		if (screenManager != null) {
			screenManager.dispose();
		}
		if (textureAtlas != null) {
			textureAtlas.dispose();
		}
		if (connection != null) {
			connection.close();
		}
		if (fontManager != null) {
			fontManager.dispose();
		}
	}

}
