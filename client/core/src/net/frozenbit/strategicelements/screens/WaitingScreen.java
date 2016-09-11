package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.google.gson.JsonObject;
import net.frozenbit.strategicelements.*;
import net.frozenbit.strategicelements.entities.Entity;
import net.frozenbit.strategicelements.widgets.BaseWidget;
import net.frozenbit.strategicelements.widgets.TextWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Screen that is shown while waiting for someone to challenge you
 */
public class WaitingScreen extends ManageableScreen implements NetworkListener {
	public static final Color[] CIRCLE_COLORS = {Color.RED, Color.OLIVE, Color.BLUE};
	private final ShapeRenderer shapeRenderer;
	private final Random random;
	private boolean matched;
	private SpriteBatch batch;
	private List<BaseWidget> widgets;
	private List<AnimatedCircle> circles;

	public WaitingScreen(StrategicElementsGame game) {
		super(game);
		matched = false;
		widgets = new ArrayList<>();
		batch = new SpriteBatch();
		circles = new ArrayList<>();
		random = new Random();
		for (int i = 0; i < 5; i++) {
			AnimatedCircle animatedCircle = new AnimatedCircle();
			resetCircle(animatedCircle);
			animatedCircle.age = -random.nextFloat() * 2f;
			circles.add(animatedCircle);
		}
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		BitmapFont font = game.getFontManager().getFont("vera/Vera.ttf", 25);
		TextWidget errorTextWidget = new TextWidget("Waiting for player...", font);
		errorTextWidget.setX(50);
		errorTextWidget.setY(Gdx.graphics.getHeight() - 50);
		widgets.add(errorTextWidget);

		game.getConnection().registerListener(this);
		game.getState().setPhase(GameState.GamePhase.WAITING);
		JsonObject request = new JsonObject();
		request.addProperty(Connection.JSON_ATTR_TYPE, Connection.JSON_TYPE_CHALLENGE);
		request.add(Connection.JSON_TYPE_NAME, null);
		game.getConnection().send(request);
	}

	private void resetCircle(AnimatedCircle animatedCircle) {
		animatedCircle.x = random.nextInt(Gdx.graphics.getWidth());
		animatedCircle.y = random.nextInt(Gdx.graphics.getHeight());
		animatedCircle.age = 0;
		animatedCircle.color = CIRCLE_COLORS[random.nextInt(CIRCLE_COLORS.length)];
	}

	@Override
	public void render(float delta) {
		if (matched) {
			game.getState().setPhase(GameState.GamePhase.BUY);
			Board board = new Board();
			Entity entity = new Entity(Entity.Type.WATER, new GridPosition(7, 2), board, game.getTextureAtlas());
			entity.setDirection(GridPosition.Direction.SOUTH_EAST);
			entity.setPartialDistance(0);
			game.getScreenManager().push(new BuyScreen(game, board));
			return;
		}

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);

		shapeRenderer.begin();
		shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
		for (AnimatedCircle circle : circles) {
			if (circle.age > 3) {
				resetCircle(circle);
			}
			if (circle.age > 0) {
				float radius = circle.age * 50f;
				float a = 1f / ((1f + circle.age) * (1f + circle.age));
				shapeRenderer.setColor(circle.color.r, circle.color.g, circle.color.b, a);
				shapeRenderer.circle(circle.x, circle.y, radius);
			}
			circle.age += delta;
		}
		shapeRenderer.end();

		batch.begin();
		for (BaseWidget widget : widgets) {
			widget.renderSprites(batch, delta);
		}
		batch.end();
	}

	@Override
	public void onDataReceived(JsonObject data) {
		String type = data.get(Connection.JSON_ATTR_TYPE).getAsString();
		if (!type.equals(Connection.JSON_TYPE_CHALLENGE))
			return;
		game.getState().setEnemyName(data.get(Connection.JSON_ATTR_NAME).getAsString());
		matched = true;
	}

	private static class AnimatedCircle {
		public float age;
		public int x, y;
		public Color color;
	}
}
