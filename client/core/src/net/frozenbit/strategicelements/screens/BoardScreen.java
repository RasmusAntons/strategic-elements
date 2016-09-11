package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.BoardRenderer;
import net.frozenbit.strategicelements.StrategicElementsGame;
import net.frozenbit.strategicelements.widgets.BaseWidget;
import net.frozenbit.strategicelements.widgets.TextWidget;

import java.util.ArrayList;
import java.util.List;


public abstract class BoardScreen extends ManageableScreen {
	protected final SpriteBatch batch;
	protected List<BaseWidget> widgets;
	protected BoardRenderer boardRenderer;
	protected Board board;

	public BoardScreen(StrategicElementsGame game, Board board) {
		super(game);
		this.board = board;
		boardRenderer = new BoardRenderer(this.board, game.getTextureAtlas());
		widgets = new ArrayList<>();
		batch = new SpriteBatch();
		BitmapFont font = game.getFontManager().getFont("vera/Vera.ttf", 16);
		TextWidget textWidget = new TextWidget("You are playing against " + game.getState().getEnemyName(), font);
		textWidget.setX(10);
		textWidget.setY(Gdx.graphics.getHeight() - 10);
		widgets.add(textWidget);

	}

	@Override
	public void render(float delta) {
		boardRenderer.render(delta);

		batch.begin();
		for (BaseWidget widget : widgets) {
			widget.renderSprites(batch, delta);
		}
		batch.end();
	}
}
