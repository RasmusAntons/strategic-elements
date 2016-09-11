package net.frozenbit.strategicelements.widgets;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TextWidget extends BaseWidget {
	private final BitmapFont font;
	private Color color = Color.BLACK;
	private GlyphLayout textLayout;
	private String text;

	public TextWidget(String text, BitmapFont font) {
		this.font = font;
		setText(text);
	}

	public void setText(String text) {
		this.text = text;
		textLayout = new GlyphLayout(font, text);
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public float getWidth() {
		return textLayout.width;
	}

	public float getHeight() {
		return textLayout.height;
	}

	@Override
	public void renderSprites(SpriteBatch batch, float delta) {
		font.setColor(color);
		font.draw(batch, text, x, y);
	}
}
