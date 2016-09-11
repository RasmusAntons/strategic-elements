package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.gson.JsonObject;
import net.frozenbit.strategicelements.*;
import net.frozenbit.strategicelements.entities.Entity;
import net.frozenbit.strategicelements.entities.PathFinder;

import java.util.Collections;
import java.util.List;

/**
 * Screen rendered during the game after the buy phase
 */
public class PlayScreen extends ManageableScreen implements NetworkListener {
	private Entity selectedEntity;
	private final SpriteBatch batch;
	private Board board;
	private BoardRenderer boardRenderer;

	public PlayScreen(Board board, BoardRenderer boardRenderer, StrategicElementsGame game) {
		super(game);
		game.getConnection().registerListener(this);
		batch = new SpriteBatch();
		this.board = board;
		this.boardRenderer = boardRenderer;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (game.getState().isTurn()) {
			if (selectedEntity != null) {

			}
		}

		boardRenderer.render(delta);

		batch.begin();
		batch.end();
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!game.getState().isTurn())
			return false;
		GridPosition touchedTilePosition = boardRenderer.mouseToGrid(screenX, screenY);
		if (!board.hasTile(touchedTilePosition))
			return false;
		switch(button) {
			case Input.Buttons.LEFT:
				if (selectedEntity == null) {
					selectedEntity = board.getEntityByPosition(touchedTilePosition);
					if (selectedEntity != null) {
						boardRenderer.setHighlightedPositions(board.getPathFinder().possibleDestinations(selectedEntity));
					} else {
						boardRenderer.getHighlightedPositions().clear();
					}
				} else {
					List<GridPosition.Direction> path = board.getPathFinder().pathTo(selectedEntity, touchedTilePosition);
					if (path != null) {

					} else {
						selectedEntity = null;
						boardRenderer.getHighlightedPositions().clear();
					}
				}
				return true;
			case Input.Buttons.RIGHT:
				selectedEntity = null;

				return true;
		}
		return false;
	}

	@Override
	public void onDataReceived(JsonObject data) {
		game.getState().setTurn(true);
	}
}
