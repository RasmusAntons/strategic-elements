package net.frozenbit.strategicelements.widgets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ButtonWidget extends TextWidget {
	private int padding = 20;
	private boolean hovered;
	private NinePatch backgroundNormal, backgroundPressed;
	private boolean pressed;
	private OnClickListener listener;

	public ButtonWidget(String text, BitmapFont font, NinePatch backgroundNormal, NinePatch backgroundPressed) {
		super(text, font);
		this.backgroundNormal = backgroundNormal;
		this.backgroundPressed = backgroundPressed;
	}

	public void setPadding(int padding) {
		this.padding = padding;
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
		if (hovered && pressed) {
			drawBackground(batch, backgroundPressed);
		} else {
			drawBackground(batch, backgroundNormal);
		}
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
	public float getWidth() {
		return super.getWidth() + 2f * padding;
	}

	@Override
	public float getHeight() {
		return super.getHeight() + 2f * padding;
	}

	public interface OnClickListener {
		void onClick(ButtonWidget widget);
	}
}
