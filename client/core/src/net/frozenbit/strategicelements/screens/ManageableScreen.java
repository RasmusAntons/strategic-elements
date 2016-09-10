package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import net.frozenbit.strategicelements.StrategicElementsGame;

public abstract class ManageableScreen implements Screen, InputProcessor {
	protected StrategicElementsGame game;

	public ManageableScreen(StrategicElementsGame game) {
		this.game = game;
	}

	/**
	 * Screens can use this method to set up additional input processors It is called
	 * before {@link #show()}.
	 * <p><strong>Note:</strong> The supplied input multiplexer already contains the
	 * screen itself.</p>
	 *
	 * @param inputMultiplexer input multiplexer to add own input processors to
	 */
	public void initInput(InputMultiplexer inputMultiplexer) {
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

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
