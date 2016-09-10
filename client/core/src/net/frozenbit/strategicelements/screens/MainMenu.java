package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MainMenu extends ManageableScreen {
	private TextureRegion background;
	private SpriteBatch batch;

	public MainMenu() {
		super();
		background = new TextureRegion(new Texture("main.png"), 0, 0, 1200, 700);
		batch = new SpriteBatch();
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 1200, 700);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0);
		batch.end();
	}
}
