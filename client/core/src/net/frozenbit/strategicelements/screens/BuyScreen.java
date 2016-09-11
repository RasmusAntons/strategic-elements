package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.frozenbit.strategicelements.*;
import net.frozenbit.strategicelements.tiles.Tile;
import net.frozenbit.strategicelements.widgets.BaseWidget;
import net.frozenbit.strategicelements.widgets.ButtonWidget;
import net.frozenbit.strategicelements.widgets.TextWidget;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * Renders the hexagonal game board and in-game GUI
 */
public class BuyScreen extends ManageableScreen implements ButtonWidget.OnClickListener {
	private final SpriteBatch batch;
	private final ButtonWidget fireButton;
	private final ButtonWidget waterButton;
	private final ButtonWidget earthButton;
	private int firePointsLeft = 10, waterPointsLeft = 10, earthPointsLeft = 10;
	private BoardRenderer boardRenderer;
	private List<BaseWidget> widgets;
	private Board board;

	public BuyScreen(StrategicElementsGame game, Board board) {
		super(game);
		this.board = board;
		boardRenderer = new BoardRenderer(board, game.getTextureAtlas());
		batch = new SpriteBatch();
		widgets = new ArrayList<>();
		BitmapFont font = game.getFontManager().getFont("vera/Vera.ttf", 16);
		BitmapFont smallFont = game.getFontManager().getFont("vera/Vera.ttf", 11);
		TextWidget textWidget = new TextWidget("You are playing against " + game.getState().getEnemyName(), font);
		textWidget.setX(10);
		textWidget.setY(Gdx.graphics.getHeight() - 10);
		widgets.add(textWidget);

		NinePatch buttonNormal = game.getTextureAtlas().createPatch("btn_default_normal");
		NinePatch buttonPressed = game.getTextureAtlas().createPatch("btn_default_pressed");

		fireButton = new ButtonWidget("Place Fire Units", font, buttonNormal, buttonPressed);
		fireButton.setTint(Color.ORANGE);
		fireButton.setX(400);
		fireButton.setY(80);
		widgets.add(fireButton);
		TextWidget fireBudgetWidget = textBelow(smallFont, fireButton);
		widgets.add(fireBudgetWidget);

		waterButton = new ButtonWidget("Place Water Units", font, buttonNormal, buttonPressed);
		waterButton.setTint(Color.SKY);
		waterButton.setX(fireButton.getX() + fireButton.getWidth() + 10);
		waterButton.setY(80);
		widgets.add(waterButton);
		TextWidget waterBudgetWidget = textBelow(smallFont, waterButton);
		widgets.add(waterBudgetWidget);

		earthButton = new ButtonWidget("Place Earth Units", font, buttonNormal, buttonPressed);
		earthButton.setTint(Color.BROWN);
		earthButton.setX(waterButton.getX() + waterButton.getWidth() + 10);
		earthButton.setY(80);
		widgets.add(earthButton);
		TextWidget earthBudgetWidget = textBelow(smallFont, earthButton);
		widgets.add(earthBudgetWidget);

		firePointsLeft = 10;
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
		if (game.getState().getPhase() == GameState.GamePhase.BUY) {
			GridPosition gridPosition = boardRenderer.mouseToGrid(screenX, screenY);
			if (!board.hasTile(gridPosition)) {
				return false;
			}
			Tile tile = board.getTile(gridPosition);
			if (tile.canWalk()) {

			}
		}
		return true;
	}

	@Override
	public void onClick(ButtonWidget widget) {
		if (widget == fireButton) {

		} else if (widget == waterButton) {

		} else if (widget == earthButton) {

		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		boardRenderer.render(delta);

		batch.begin();
		switch (game.getState().getPhase()) {
			case BUY:
				renderBuyPhase(delta);
				break;
			case PLAY:
				break;
			case OVER:
				break;
			default:
				break;
		}
		for (BaseWidget widget : widgets) {
			widget.renderSprites(batch, delta);
		}
		batch.end();
	}

	private void renderBuyPhase(float delta) {

	}

}
