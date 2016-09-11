package net.frozenbit.strategicelements;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import net.frozenbit.strategicelements.entities.Entity;
import net.frozenbit.strategicelements.tiles.Tile;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoardRenderer {
	private static final float TILE_EDGE_LEN = 20.0f;
	private static final float TILE_HEIGHT = (float) (TILE_EDGE_LEN * Math.sqrt(3.0));
	private static final Vector2 X_AXIS_VEC = new Vector2(-1, 0).rotate(150).scl(TILE_HEIGHT);
	private static final Vector2 Y_AXIS_VEC = new Vector2(0, -1).scl(TILE_HEIGHT);
	private static final Vector2 Z_AXIS_VEC = new Vector2(1, 0).rotate(30).scl(TILE_HEIGHT);

	private final OrthographicCamera camera;
	private final SpriteBatch spriteBatch;
	private final TextureAtlas.AtlasRegion outLine;
	private final Array<TextureAtlas.AtlasRegion> groundTextures;
	private final TextureAtlas.AtlasRegion overlayTexture;
	private final Array<TextureAtlas.AtlasRegion> levelOverlays;
	private Set<GridPosition> highlightedPositions;
	private Board board;

	public BoardRenderer(Board board, TextureAtlas atlas) {
		camera = new OrthographicCamera(1200, 700);
		this.board = board;
		spriteBatch = new SpriteBatch();
		outLine = atlas.findRegion("outline");
		groundTextures = atlas.findRegions("ground");
		overlayTexture = atlas.findRegion("overlay");
		levelOverlays = atlas.findRegions("level_overlay");
		highlightedPositions = new HashSet<>();
	}

	public void render(float delta) {
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		GridPosition hoverPosition = mouseToGrid(x, y);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for (Map.Entry<GridPosition, Tile> tileEntry : board.getTiles()) {
			GridPosition position = tileEntry.getKey();
			Vector2 center = center(position);
			TextureRegion region = groundTextures.first();
			drawTexture(region, center.x, center.y);
			if (highlightedPositions.contains(position)) {
				spriteBatch.setColor(0, 1, 0, 0.1f);
				drawTexture(overlayTexture, center.x, center.y);
				spriteBatch.setColor(Color.WHITE);
			}
			if (hoverPosition.equals(position)) {
				spriteBatch.setColor(0, 0, 1, 0.1f);
				drawTexture(overlayTexture, center.x, center.y);
				spriteBatch.setColor(Color.WHITE);
			}
			drawTexture(outLine, center.x, center.y);
		}
		for (Entity entity : board.getEntities()) {
			entity.onTick(delta);
			TextureRegion texture = entity.getTexture();
			Vector2 offset = offset(entity.getDirection(), entity.getPartialDistance());
			Vector2 renderPosition = center(entity.getRenderPosition()).add(offset);
			drawTexture(texture, renderPosition.x, renderPosition.y);
			drawTexture(levelOverlays.get(entity.getLevel() - 1), renderPosition.x, renderPosition.y);
			if (!entity.isEnemy()) {
				Vector2 center = center(entity.getPosition());
				spriteBatch.setColor(0, 0.9f, 0, 1);
				drawTexture(outLine, center.x, center.y);
				spriteBatch.setColor(Color.WHITE);
			}
		}
		spriteBatch.end();
	}

	private void drawTexture(TextureRegion texture, float x, float y) {
		spriteBatch.draw(texture, x - texture.getRegionWidth() / 2, y - texture.getRegionHeight() / 2);
	}

	public Set<GridPosition> getHighlightedPositions() {
		return highlightedPositions;
	}

	public void setHighlightedPositions(Set<GridPosition> positions) {
		this.highlightedPositions = positions;
	}

	/**
	 * Convert input coordinates (mouse, touchscreen). Formula taken from this
	 * <a href="http://www.redblobgames.com/grids/hexagons/#conversions">helpful website</a>
	 *
	 * @param x x coordinate
	 * @param y y coordinate
	 * @return GridPosition that is hovered. It can be no tile at this position
	 */
	public GridPosition mouseToGrid(int x, int y) {
		Vector3 worldPos = camera.unproject(new Vector3(x, y, 0));
		x = (int) worldPos.x;
		y = (int) worldPos.y;
		float q = x * 2f / 3f / TILE_EDGE_LEN;
		float r = (float) ((-x / 3f + Math.sqrt(3) / 3f * -y) / TILE_EDGE_LEN);
		return GridPosition.axialRound(q, r);
	}

	private Vector2 center(GridPosition pos) {
		return new Vector2(0, 0)
				.mulAdd(X_AXIS_VEC, pos.getX())
				.mulAdd(Y_AXIS_VEC, pos.getY());
	}

	private Vector2 offset(GridPosition.Direction direction, float partialDistance) {
		Vector2 directionVector;
		switch (direction) {
			case NORTH:
				directionVector = Y_AXIS_VEC.cpy().scl(-1);
				break;
			case NORTH_EAST:
				directionVector = Z_AXIS_VEC.cpy();
				break;
			case SOUTH_EAST:
				directionVector = X_AXIS_VEC.cpy();
				break;
			case SOUTH:
				directionVector = Y_AXIS_VEC.cpy();
				break;
			case SOUTH_WEST:
				directionVector = Z_AXIS_VEC.cpy().scl(-1);
				break;
			case NORTH_WEST:
				directionVector = X_AXIS_VEC.cpy().scl(-1);
				break;
			default:
				// this should never happen
				throw new AssertionError("Invalid direction");
		}
		return directionVector.scl(partialDistance);
	}
}
