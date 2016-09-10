package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Screen;
import net.frozenbit.strategicelements.StrategicElementsGame;

public abstract class ManageableScreen implements Screen {
	protected StrategicElementsGame game;

	public ManageableScreen(StrategicElementsGame game) {
		this.game = game;
	}

	@Override
	public void show() {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}
}
