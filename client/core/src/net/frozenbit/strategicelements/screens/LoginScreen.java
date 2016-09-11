package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.JsonObject;
import net.frozenbit.strategicelements.Connection;
import net.frozenbit.strategicelements.GameState;
import net.frozenbit.strategicelements.NetworkListener;
import net.frozenbit.strategicelements.StrategicElementsGame;

public class LoginScreen extends ManageableScreen implements Input.TextInputListener, NetworkListener {

	private Connection connection;
	private TextureRegion background;
	private SpriteBatch batch;
	private boolean waiting;

	public LoginScreen(StrategicElementsGame game) {
		super(game);
		game.getConnection().registerListener(this);
		this.connection = game.getConnection();
		background = game.getTextureAtlas().findRegion("main");
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 1200, 700);
		waiting = false;
	}

	@Override
	public void input(String text) {
		game.getState().setName(text);
		JsonObject request = new JsonObject();
		request.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_NAME);
		request.addProperty(Connection.JSON_ATTR_NAME, text);
		connection.send(request);
	}

	@Override
	public void canceled() {
		waiting = false;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();
		if (game.getState().isLoggedIn()) {
			game.getScreenManager().swap(new MainMenu(game));
			game.getState().setPhase(GameState.GamePhase.READY);
			System.out.println("logged in as " + game.getState().getName());
		} else if (!waiting) {
			Gdx.input.getTextInput(this, "Enter your name", "", "Name");
			waiting = true;
		}
	}

	@Override
	public void onDataReceived(JsonObject data) {
		String type = data.get(Connection.JSON_ATTR_TYPE).getAsString();
		if (!type.equals(Connection.JSON_TYPE_NAME))
			return;
		boolean success = data.get(Connection.JSON_ATTR_SUCCESS).getAsBoolean();
		if (success) {
			game.getState().setLoggedIn(true);
		} else {
			waiting = false;
		}
	}
}
