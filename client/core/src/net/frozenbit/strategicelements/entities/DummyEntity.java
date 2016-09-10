package net.frozenbit.strategicelements.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import net.frozenbit.strategicelements.Board;

public class DummyEntity extends Entity {
	private static final String TEXTURE_NAME = "dummyentity";

	TextureAtlas.AtlasRegion texture;

	public DummyEntity(Board board, TextureAtlas atlas) {
		super(board);
		texture = atlas.findRegion(TEXTURE_NAME);
	}

	@Override
	public TextureRegion getTexture() {
		return texture;
	}
}
