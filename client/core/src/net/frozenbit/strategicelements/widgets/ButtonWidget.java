package net.frozenbit.strategicelements.widgets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class ButtonWidget extends TextWidget {
	private int padding = 10;

	public ButtonWidget(String text, BitmapFont font) {
		super(text, font);
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false; // todo
	}

	@Override
	public void renderSprites(SpriteBatch batch, float delta) {
		x += padding;
		y += padding;
		super.renderSprites(batch, delta);
		x -= padding;
		y -= padding;
	}

	@Override
	public void renderShapes(ShapeRenderer batch, float delta) {
		super.renderShapes(batch, delta);
		batch.setColor(Color.BROWN);
		batch.rect(x, y - super.getHeight(), getWidth(), getHeight());
	}

	@Override
	public float getWidth() {
		return super.getWidth() + 2 * padding;
	}

	@Override
	public float getHeight() {
		return super.getHeight() + 2 * padding;
	}
}
