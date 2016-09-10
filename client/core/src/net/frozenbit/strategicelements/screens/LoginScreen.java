package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoginScreen extends ManageableScreen implements Input.TextInputListener {
	private TextureRegion background;
	private SpriteBatch batch;
	private boolean waiting;

	public LoginScreen() {
		super();
		background = new TextureRegion(new Texture("main.png"), 0, 0, 1200, 700);
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 1200, 700);
		waiting = false;
	}

	@Override
	public void input(String text) {
		System.out.println("You entered " + text);
	}

	@Override
	public void canceled() {
		System.out.println("Canceled!");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();
		if (!waiting) {
			Gdx.input.getTextInput(this, "Enter your name", "", "Name");
			waiting = true;
		}
	}
}
