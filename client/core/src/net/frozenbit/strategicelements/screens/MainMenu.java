package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
import java.util.Random;

public class MainMenu extends ManageableScreen {
	private List<BaseWidget> widgets;
	private TextureRegion background;
	private SpriteBatch batch;

	public MainMenu(StrategicElementsGame game) {
		super(game);
		widgets = new ArrayList<>();
		background = game.getTextureAtlas().findRegion("main");
		batch = new SpriteBatch();
		//batch.getProjectionMatrix().setToOrtho2D(0, 0, 1200, 700);
		BitmapFont font = game.getFontManager().getFont("vera/Vera.ttf", 18);
		TextWidget textWidget = new TextWidget("lelellelelelelelel", font);
		textWidget.setX(100);
		textWidget.setY(600);
		widgets.add(textWidget);
		NinePatch btnNormal = game.getTextureAtlas().createPatch("btn_default_normal");
		NinePatch btnPressed = game.getTextureAtlas().createPatch("btn_default_pressed");
		ButtonWidget buttonWidget = new ButtonWidget("Click plz", font, btnNormal, btnPressed);
		buttonWidget.setX(400);
		buttonWidget.setY(600);
		buttonWidget.setOnClickListener(new ButtonWidget.OnClickListener() {
			@Override
			public void onClick(ButtonWidget widget) {
				Random random = new Random();
				Gdx.gl.glClearColor(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
			}
		});
		widgets.add(buttonWidget);
		Gdx.gl.glClearColor(1, 1, 1, 1);
	}

	@Override
	public void initInput(InputMultiplexer inputMultiplexer) {
		for (BaseWidget widget : widgets) {
			inputMultiplexer.addProcessor(widget);
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(background, 0, 0);
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
