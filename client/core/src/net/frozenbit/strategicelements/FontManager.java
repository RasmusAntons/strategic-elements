package net.frozenbit.strategicelements;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;
import java.util.Map;

public class FontManager {
	private Map<FontInfo, BitmapFont> fontCache;

	public FontManager() {
		fontCache = new HashMap<>();
	}

	public BitmapFont getFont(String file, int size) {
		FontInfo fontInfo = new FontInfo(file, size);
		BitmapFont font = fontCache.get(fontInfo);
		if (font != null) {
			return font;
		}
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = fontInfo.size;
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + fontInfo.file));
		font = generator.generateFont(parameter);
		generator.dispose();
		fontCache.put(fontInfo, font);
		return font;
	}

	public void dispose() {
		for (BitmapFont font : fontCache.values()) {
			font.dispose();
		}
	}

	private static class FontInfo {
		public final String file;
		public final int size;

		public FontInfo(String file, int size) {
			this.file = file;
			this.size = size;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			FontInfo fontInfo = (FontInfo) o;

			return size == fontInfo.size && (file != null ? file.equals(fontInfo.file) : fontInfo.file == null);

		}

		@Override
		public int hashCode() {
			int result = file != null ? file.hashCode() : 0;
			result = 31 * result + size;
			return result;
		}
	}
}
