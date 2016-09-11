package net.frozenbit.strategicelements.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import net.frozenbit.strategicelements.StrategicElementsGame;
import net.frozenbit.strategicelements.widgets.BaseWidget;
import net.frozenbit.strategicelements.widgets.ButtonWidget;
import net.frozenbit.strategicelements.widgets.TextWidget;

import java.util.ArrayList;
import java.util.List;

public class MainMenu extends ManageableScreen {
	private final ShapeRenderer shapeRenderer;
	private List<BaseWidget> widgets;
	private TextureRegion background;
	private SpriteBatch batch;

	public MainMenu(StrategicElementsGame game) {
		super(game);
		widgets = new ArrayList<>();
		background = game.getTextureAtlas().findRegion("main");
		batch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);
		batch.getProjectionMatrix().setToOrtho2D(0, 0, 1200, 700);
		shapeRenderer.getProjectionMatrix().setToOrtho2D(0, 0, 1200, 700);
		BitmapFont font = game.getFontManager().getFont("vera/Vera.ttf", 18);
		TextWidget textWidget = new TextWidget("lelellelelelelelel", font);
		textWidget.setX(100);
		textWidget.setY(600);
		widgets.add(textWidget);
		ButtonWidget buttonWidget = new ButtonWidget("Click plz", font);
		buttonWidget.setX(400);
		buttonWidget.setY(600);
		widgets.add(buttonWidget);
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

		shapeRenderer.begin();
		for (BaseWidget widget : widgets) {
			widget.renderShapes(shapeRenderer, delta);
		}
		shapeRenderer.end();

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
		}
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

}
