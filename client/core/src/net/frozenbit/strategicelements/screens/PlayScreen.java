package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.google.gson.JsonObject;
import net.frozenbit.strategicelements.*;
import net.frozenbit.strategicelements.entities.Entity;

import java.util.List;

/**
 * Screen rendered during the game after the buy phase
 */
public class PlayScreen extends BoardScreen implements NetworkListener {
	private Entity selectedEntity;

	public PlayScreen(Board board, BoardRenderer boardRenderer, StrategicElementsGame game) {
		super(game, board);
		game.getConnection().registerListener(this);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (game.getState().isTurn()) {
			if (selectedEntity != null) {

			}
		}

		super.render(delta);

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
