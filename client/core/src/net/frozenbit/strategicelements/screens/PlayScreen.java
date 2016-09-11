package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import net.frozenbit.strategicelements.*;
import net.frozenbit.strategicelements.entities.Entity;

import java.util.Queue;
import java.util.List;
import java.util.Queue;

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
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (!game.getState().isTurn()) {
			System.out.println("it's not your turn");
			return false;
		}
		GridPosition touchedTilePosition = boardRenderer.mouseToGrid(screenX, screenY);
		if (!board.hasTile(touchedTilePosition)) {
			System.out.println("not a tile");
			return false;
		}
		switch(button) {
			case Input.Buttons.LEFT:
				if (selectedEntity == null) {
					Entity clickedEntity = board.getEntityByPosition(touchedTilePosition);
					if (clickedEntity != null && !clickedEntity.isEnemy()) {
						selectedEntity = clickedEntity;
						boardRenderer.setHighlightedPositions(board.getPathFinder().possibleDestinations(selectedEntity));
					}
				} else {
					Queue<GridPosition.Direction> path = board.getPathFinder().pathTo(selectedEntity, touchedTilePosition);
					if (path != null) {
						GridPosition origin = selectedEntity.getPosition();
						moveEntity(selectedEntity, touchedTilePosition, path);
						game.getState().setTurn(false);
						sendTurn(origin, touchedTilePosition);
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

	private void moveEntity(Entity entity, GridPosition destination, Queue<GridPosition.Direction> path) {
		Entity enemy = board.getEntityByPosition(destination);
		if (enemy != null) {
			if (entity.winsAgainst(enemy)) {
				enemy.dispose();
			} else {
				entity.dispose();
				return;
			}
		}
		entity.move(destination, path);
	}

	private void sendTurn(GridPosition departure, GridPosition destination) {
		JsonObject request = new JsonObject();
		request.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_TURN);
		JsonObject jsonTurn = new JsonObject();
		jsonTurn.addProperty("depX", departure.getX());
		jsonTurn.addProperty("depY", departure.getY());
		jsonTurn.addProperty("destX", destination.getX());
		jsonTurn.addProperty("destY", destination.getY());
		request.add("turn", jsonTurn);
	}

	@Override
	public void onDataReceived(JsonObject data) {
		String type = data.get(Connection.JSON_ATTR_TYPE).getAsString();
		switch(type) {
			case Connection.JSON_TYPE_TURN:
				JsonObject jsonTurn = data.get("turn").getAsJsonObject();
				GridPosition origin = new GridPosition(jsonTurn.get("depX").getAsInt(), jsonTurn.get("depY").getAsInt());
				GridPosition destination = new GridPosition(jsonTurn.get("depX").getAsInt(), jsonTurn.get("depY").getAsInt());
				Entity movedEntity = board.getEntityByPosition(origin);
				if (movedEntity == null)
					throw new RuntimeException("movement hacks detected");
				Queue<GridPosition.Direction> path = board.getPathFinder().pathTo(movedEntity, destination);
				if (path == null)
					throw new RuntimeException("movement hacks detected");
				moveEntity(movedEntity, destination, path);
				game.getState().setTurn(true);
				break;
			case Connection.JSON_TYPE_CLOSE:
				break;
		}
	}
}
