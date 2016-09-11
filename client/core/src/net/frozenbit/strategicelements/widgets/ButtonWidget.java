package net.frozenbit.strategicelements.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ButtonWidget extends TextWidget {
	private int padding = 20;
	private boolean hovered;
	private NinePatch backgroundNormal, backgroundPressed;
	private boolean pressed;
	private OnClickListener listener;
	private Color tint = Color.WHITE;

	public ButtonWidget(String text, BitmapFont font, NinePatch backgroundNormal, NinePatch backgroundPressed) {
		super(text, font);
		this.backgroundNormal = backgroundNormal;
		this.backgroundPressed = backgroundPressed;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public void setTint(Color tint) {
		this.tint = tint;
	}

	public void setOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		checkHover(screenX, screenY);
		return hovered;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		checkHover(screenX, screenY);
		return hovered;
	}

	private void checkHover(int screenX, int screenY) {
		screenY = Gdx.graphics.getHeight() - screenY;
		hovered = x <= screenX && screenX < x + getWidth()
				&& y >= screenY && screenY > y - getHeight();
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		pressed = hovered;
		return pressed;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (hovered && pressed && listener != null) {
			listener.onClick(this);
		}
		pressed = false;
		return false;
	}

	@Override
	public void renderSprites(SpriteBatch batch, float delta) {
		batch.setColor(tint);
		if (hovered && pressed) {
			drawBackground(batch, backgroundPressed);
		} else {
			drawBackground(batch, backgroundNormal);
		}
		batch.setColor(Color.WHITE);
		x += padding;
		y -= padding;
		super.renderSprites(batch, delta);
		x -= padding;
		y += padding;
	}

	private void drawBackground(SpriteBatch batch, NinePatch background) {
		background.draw(batch, x, y - getHeight(), getWidth(), getHeight());
	}


	@Override
	public int getWidth() {
		return super.getWidth() + 2 * padding;
	}

	@Override
	public int getHeight() {
		return super.getHeight() + 2 * padding;
	}

	public interface OnClickListener {
		void onClick(ButtonWidget widget);
	}
}
