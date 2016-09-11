package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.GridPosition;
import net.frozenbit.strategicelements.StrategicElementsGame;
import net.frozenbit.strategicelements.entities.DummyEntity;
import net.frozenbit.strategicelements.widgets.BaseWidget;
import net.frozenbit.strategicelements.widgets.ButtonWidget;
import net.frozenbit.strategicelements.widgets.TextWidget;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends ManageableScreen {
	private List<BaseWidget> widgets;
	private TextureRegion background;
	private SpriteBatch batch;

	public MainMenu(final StrategicElementsGame game) {
		super(game);
		widgets = new ArrayList<>();
		background = game.getTextureAtlas().findRegion("main");
		batch = new SpriteBatch();

		BitmapFont font = game.getFontManager().getFont("vera/Vera.ttf", 25);
		if (!game.getConnection().isOnline()) {
			TextWidget errorTextWidget = new TextWidget("Connection to the server failed :(", font);
			errorTextWidget.setColor(Color.FIREBRICK);
			errorTextWidget.setX(400);
			errorTextWidget.setY(90);
			widgets.add(errorTextWidget);
		} else {
			NinePatch btnNormal = game.getTextureAtlas().createPatch("btn_default_normal");
			NinePatch btnPressed = game.getTextureAtlas().createPatch("btn_default_pressed");
			ButtonWidget buttonWidget = new ButtonWidget("Play now", font, btnNormal, btnPressed);
			buttonWidget.setX(500);
			buttonWidget.setY(100);
			buttonWidget.setOnClickListener(new ButtonWidget.OnClickListener() {
				@Override
				public void onClick(ButtonWidget widget) {
					game.getScreenManager().push(new WaitingScreen(game));
				}
			});
			widgets.add(buttonWidget);
		}
	}

	@Override
	public void initInput(InputMultiplexer inputMultiplexer) {
		for (BaseWidget widget : widgets) {
			inputMultiplexer.addProcessor(widget);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background,
				(Gdx.graphics.getWidth() - background.getRegionWidth()) / 2,
				(Gdx.graphics.getHeight() - background.getRegionHeight()) / 2);
		for (BaseWidget widget : widgets) {
			widget.renderSprites(batch, delta);
		}
		batch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Input.Keys.ESCAPE) {
			game.getScreenManager().pop();
			return true;
		} else if (keycode == Input.Keys.SPACE) {
			Board board = new Board();
			DummyEntity entity = new DummyEntity(board, game.getTextureAtlas());
			entity.setPosition(new GridPosition(7, 2));
			entity.setDirection(GridPosition.Direction.SOUTH_EAST);
			entity.setPartialDistance(0);
			game.getScreenManager().push(new BoardScreen(game, board));
			return true;
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

}
