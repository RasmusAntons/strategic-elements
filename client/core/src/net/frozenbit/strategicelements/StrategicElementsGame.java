package net.frozenbit.strategicelements;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.frozenbit.strategicelements.screens.BoardScreen;
import net.frozenbit.strategicelements.screens.LoginScreen;
import net.frozenbit.strategicelements.screens.ScreenManager;

public class StrategicElementsGame extends ApplicationAdapter {
	private Connection connection;
	private TextureAtlas textureAtlas;

	private ScreenManager screenManager;

	@Override
	public void create() {
		initState();
		screenManager.push(new LoginScreen(this));
		screenManager.push(new BoardScreen(this, new Board()));
	}

	private void initState() {
		try {
			connection = new Connection("localhost", 5123);
		} catch(GdxRuntimeException e) {
			e.printStackTrace();
			//System.exit(-1);
		}
		textureAtlas = new TextureAtlas(Gdx.files.internal("textures.atlas"));
		screenManager = new ScreenManager();
	}

	public TextureAtlas getTextureAtlas() {
		return textureAtlas;
	}

	public Connection getConnection() {
		return connection;
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
	}

}
