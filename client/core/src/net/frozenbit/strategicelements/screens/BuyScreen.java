package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.frozenbit.strategicelements.*;
import net.frozenbit.strategicelements.entities.Entity;
import net.frozenbit.strategicelements.tiles.Tile;
import net.frozenbit.strategicelements.widgets.BaseWidget;
import net.frozenbit.strategicelements.widgets.ButtonWidget;
import net.frozenbit.strategicelements.widgets.TextWidget;

import static java.lang.String.format;

/**
 * Renders the hexagonal game board and in-game GUI.
 */
// the code of this class is quite messy and should be cleaned up in the future
public class BuyScreen extends BoardScreen implements ButtonWidget.OnClickListener, NetworkListener {
	private static final String POINTS_LEFT_STR = "Place %d more";
	private final ButtonWidget fireButton;
	private final ButtonWidget waterButton;
	private final ButtonWidget earthButton;
	private final TextWidget fireBudgetWidget;
	private final TextWidget waterBudgetWidget;
	private final TextWidget earthBudgetWidget;
	private boolean play;
	private JsonArray jsonEnemyEntities;
	private int firePointsLeft = 10, waterPointsLeft = 10, earthPointsLeft = 10;
	private Entity.Type currentEntityType;

	public BuyScreen(StrategicElementsGame game, Board board) {
		super(game, board);
		game.getConnection().registerListener(this);
		play = false;

		NinePatch buttonNormal = game.getTextureAtlas().createPatch("btn_default_normal");
		NinePatch buttonPressed = game.getTextureAtlas().createPatch("btn_default_pressed");

		BitmapFont font = game.getFontManager().getFont("vera/Vera.ttf", 16);
		BitmapFont smallFont = game.getFontManager().getFont("vera/Vera.ttf", 11);

		fireButton = new ButtonWidget("Place Fire Units", font, buttonNormal, buttonPressed);
		fireButton.setTint(Color.ORANGE);
		fireButton.setX(400);
		fireButton.setY(80);
		fireButton.setOnClickListener(this);
		widgets.add(fireButton);
		fireBudgetWidget = textBelow(smallFont, fireButton);
		widgets.add(fireBudgetWidget);

		waterButton = new ButtonWidget("Place Water Units", font, buttonNormal, buttonPressed);
		waterButton.setTint(Color.SKY);
		waterButton.setX(fireButton.getX() + fireButton.getWidth() + 10);
		waterButton.setY(80);
		waterButton.setOnClickListener(this);
		widgets.add(waterButton);
		waterBudgetWidget = textBelow(smallFont, waterButton);
		widgets.add(waterBudgetWidget);

		earthButton = new ButtonWidget("Place Earth Units", font, buttonNormal, buttonPressed);
		earthButton.setTint(Color.BROWN);
		earthButton.setX(waterButton.getX() + waterButton.getWidth() + 10);
		earthButton.setY(80);
		earthButton.setOnClickListener(this);
		widgets.add(earthButton);
		earthBudgetWidget = textBelow(smallFont, earthButton);
		widgets.add(earthBudgetWidget);

		ButtonWidget doneButton = new ButtonWidget("Done", font, buttonNormal, buttonPressed);
		doneButton.setX(earthButton.getX() + earthButton.getWidth() + 100);
		doneButton.setY(80);
		doneButton.setOnClickListener(new ButtonWidget.OnClickListener() {
			private boolean alreadyDone;

			@Override
			public void onClick(ButtonWidget widget) {
				if (alreadyDone) {
					return;
				}
				alreadyDone = true;
				sendBuy();
			}
		});
		widgets.add(doneButton);


		firePointsLeft = 10;
		waterPointsLeft = 10;
		earthPointsLeft = 10;
		updateBudgets();
	}

	private void updateBudgets() {
		fireBudgetWidget.setText(format(POINTS_LEFT_STR, firePointsLeft));
		waterBudgetWidget.setText(format(POINTS_LEFT_STR, waterPointsLeft));
		earthBudgetWidget.setText(format(POINTS_LEFT_STR, earthPointsLeft));
	}

	private TextWidget textBelow(BitmapFont font, BaseWidget widget) {
		TextWidget fireBudgetWidget = new TextWidget(format("Place %d more", 10), font);
		fireBudgetWidget.setColor(Color.DARK_GRAY);
		fireBudgetWidget.setX(widget.getX() + 8);
		fireBudgetWidget.setY(widget.getY() - widget.getHeight());
		return fireBudgetWidget;
	}

	@Override
	public void initInput(InputMultiplexer inputMultiplexer) {
		for (BaseWidget widget : widgets) {
			inputMultiplexer.addProcessor(widget);
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (currentEntityType == null) {
			return true;
		}

		GridPosition gridPosition = boardRenderer.mouseToGrid(screenX, screenY);
		if (!board.hasTile(gridPosition)) {
			return false;
		}

		Tile tile = board.getTile(gridPosition);
		if (!tile.canWalk()) {
			return false;
		}

		Entity entity = board.getEntityByPosition(gridPosition);
		if (entity != null && entity.getType() != currentEntityType) {
			return false;
		}

		if (currentEntityType == Entity.Type.FIRE) {
			if (firePointsLeft > 0) {
				firePointsLeft--;
			} else {
				return false;
			}
		}

		if (currentEntityType == Entity.Type.WATER) {
			if (waterPointsLeft > 0) {
				waterPointsLeft--;
			} else {
				return false;
			}
		}
		if (currentEntityType == Entity.Type.EARTH) {
			if (earthPointsLeft > 0) {
				earthPointsLeft--;
			} else {
				return false;
			}
		}
		updateBudgets();

		if (entity == null) {
			entity = new Entity(currentEntityType, gridPosition, board, game.getTextureAtlas());
		} else if (entity.getLevel() < Entity.MAX_LEVEL) {
			entity.setLevel(entity.getLevel() + 1);
		}
		return true;
	}

	@Override
	public void onClick(ButtonWidget widget) {
		if (widget == fireButton) {
			currentEntityType = Entity.Type.FIRE;
		} else if (widget == waterButton) {
			currentEntityType = Entity.Type.WATER;
		} else if (widget == earthButton) {
			currentEntityType = Entity.Type.EARTH;
		}
	}

	@Override
	public void render(float delta) {
		if (play) {
			game.getScreenManager().push(new PlayScreen(board, boardRenderer, game));
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		super.render(delta);

		batch.begin();
		batch.end();
	}

	private void sendBuy() {
		JsonObject request = new JsonObject();
		request.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_BUY);
		JsonArray jsonEntities = new JsonArray();
		for (Entity entity : board.getEntities()) {
			JsonObject jsonEntity = new JsonObject();
			jsonEntity.addProperty("x", entity.getPosition().getX());
			jsonEntity.addProperty("y", entity.getPosition().getY());
			jsonEntity.addProperty("lvl", entity.getLevel());
			switch (entity.getType()) {
				case FIRE:
					jsonEntity.addProperty("type", "fire");
					break;
				case WATER:
					jsonEntity.addProperty("type", "water");
					break;
				case EARTH:
					jsonEntity.addProperty("type", "earth");
					break;
				default:
					throw new AssertionError("invalid Type");
			}
			jsonEntities.add(jsonEntity);
		}
		request.add(Connection.JSON_ATTR_ENTITIES, jsonEntities);
		game.getConnection().send(request);
	}

	@Override
	public void onDataReceived(JsonObject data) {
		String type = data.get(Connection.JSON_ATTR_TYPE).getAsString();
		switch(type) {
			case Connection.JSON_TYPE_BUY:
				jsonEnemyEntities = data.get(Connection.JSON_ATTR_ENTITIES).getAsJsonArray();
				break;
			case Connection.JSON_TYPE_PLAY:
				for (JsonElement jsonElement : jsonEnemyEntities) {
					JsonObject jsonEntity = jsonElement.getAsJsonObject();
					GridPosition position = new GridPosition(jsonEntity.get("x").getAsInt(), jsonEntity.get("y").getAsInt());
					int level = jsonEntity.get("lvl").getAsInt();
					Entity.Type entityType;
					switch (jsonEntity.get("type").getAsString()) {
						case "fire":
							entityType = Entity.Type.FIRE;
							break;
						case "water":
							entityType = Entity.Type.WATER;
							break;
						case "earth":
							entityType = Entity.Type.EARTH;
							break;
						default:
							throw new AssertionError("invalid Type");
					}
					Entity entity = new Entity(entityType, position, board, game.getTextureAtlas());
					entity.setLevel(level);
					entity.setEnemy(true);
				}
				game.getState().setTurn(data.get(Connection.JSON_ATTR_SUCCESS).getAsBoolean());
				game.getState().setPhase(GameState.GamePhase.PLAY);
				play = true;
				break;
		}
	}
}
