package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import net.frozenbit.strategicelements.*;
import net.frozenbit.strategicelements.entities.Entity;

import java.util.ArrayDeque;
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
					if (board.getPathFinder().possibleDestinations(selectedEntity).contains(touchedTilePosition)) {
						GridPosition origin = selectedEntity.getPosition();
						moveEntity(selectedEntity, touchedTilePosition);
						game.getState().setTurn(false);
						sendTurn(origin, touchedTilePosition);
						selectedEntity = null;
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

	private void moveEntity(Entity entity, GridPosition destination) {
		Entity enemy = board.getEntityByPosition(destination);
		if (enemy != null) {
			if (entity.winsAgainst(enemy)) {
				enemy.dispose();
			} else {
				entity.dispose();
				return;
			}
		}
		entity.setPosition(destination);
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
		game.getConnection().send(request);
	}

	@Override
	public void onDataReceived(JsonObject data) {
		String type = data.get(Connection.JSON_ATTR_TYPE).getAsString();
		switch(type) {
			case Connection.JSON_TYPE_TURN:
				JsonObject jsonTurn = data.get("turn").getAsJsonObject();
				GridPosition origin = new GridPosition(jsonTurn.get("depX").getAsInt(), jsonTurn.get("depY").getAsInt());
				GridPosition destination = new GridPosition(jsonTurn.get("destX").getAsInt(), jsonTurn.get("destY").getAsInt());
				Entity movedEntity = board.getEntityByPosition(origin);
				if (movedEntity == null)
					throw new RuntimeException("movement hacks detected");
				moveEntity(movedEntity, destination);
				game.getState().setTurn(true);
				break;
			case Connection.JSON_TYPE_CLOSE:
				break;
		}
	}
}
