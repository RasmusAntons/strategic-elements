package net.frozenbit.strategicelements;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.frozenbit.strategicelements.entities.DummyEntity;
import net.frozenbit.strategicelements.screens.BoardScreen;
import net.frozenbit.strategicelements.screens.MainMenu;
import net.frozenbit.strategicelements.screens.ScreenManager;

public class StrategicElementsGame extends ApplicationAdapter {
	private Connection connection;
	private TextureAtlas textureAtlas;

	private ScreenManager screenManager;

	@Override
	public void create() {
		initState();
		screenManager.push(new MainMenu(this));
		//screenManager.push(new LoginScreen(this));
		Board board = new Board();
		DummyEntity entity = new DummyEntity(board, textureAtlas);
		entity.setPosition(new GridPosition(7, 2));
		entity.setDirection(GridPosition.Direction.SOUTH_WEST);
		entity.setPartialDistance(0);
		screenManager.push(new BoardScreen(this, board));
	}

	private void initState() {
		try {
			connection = new Connection("raspi.rasmusantons.de", 5123);
		} catch (GdxRuntimeException e) {
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

	public ScreenManager getScreenManager() {
		return screenManager;
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
