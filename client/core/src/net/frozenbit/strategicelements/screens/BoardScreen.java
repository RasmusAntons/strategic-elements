package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.BoardRenderer;
import net.frozenbit.strategicelements.GameState;
import net.frozenbit.strategicelements.StrategicElementsGame;

/**
 * Renders the hexagonal game board and in-game GUI
 */
public class BoardScreen extends ManageableScreen {
	private BoardRenderer boardRenderer;

	public BoardScreen(StrategicElementsGame game, Board board) {
		super(game);
		boardRenderer = new BoardRenderer(board, game.getTextureAtlas());
	}

	@Override
	public void render(float delta) {
		switch (game.getState().getPhase()) {
			case INIT:
				break;
			case READY:
				game.getState().setPhase(GameState.GamePhase.BUY);
				break;
			case BUY:
				break;
			case PLAY:
				break;
			case OVER:
				break;
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		boardRenderer.render(delta);
	}
}
