package net.frozenbit.strategicelements;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import net.frozenbit.strategicelements.entities.Entity;
import net.frozenbit.strategicelements.tiles.Tile;

import java.util.Map;

public class BoardRenderer {
	private static final float TILE_EDGE_LEN = 20.0f;
	private static final float TILE_HEIGHT = (float) (TILE_EDGE_LEN * Math.sqrt(3.0));
	private static final Vector2 X_AXIS_VEC = new Vector2(-1, 0).rotate(150).scl(TILE_HEIGHT);
	private static final Vector2 Y_AXIS_VEC = new Vector2(0, -1).scl(TILE_HEIGHT);
	private static final Vector2 Z_AXIS_VEC = new Vector2(1, 0).rotate(30).scl(TILE_HEIGHT);

	private final OrthographicCamera camera;
	private final SpriteBatch spriteBatch;
	private final TextureAtlas.AtlasRegion outLine;
	private Board board;
	private long count;

	public BoardRenderer(Board board, TextureAtlas atlas) {
		camera = new OrthographicCamera(1200, 700);
		this.board = board;
		spriteBatch = new SpriteBatch();
		outLine = atlas.findRegion("outline");
	}

	public void render(float delta) {
		int x = Gdx.input.getX();
		int y = Gdx.input.getY();
		Vector3 mouse = camera.unproject(new Vector3(x, y, 0));
		GridPosition gridPosition = pixelToGrid((int) mouse.x, (int) mouse.y);
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for (Map.Entry<GridPosition, Tile> tileEntry : board.getTiles()) {
			Vector2 center = center(tileEntry.getKey());
			if (tileEntry.getKey().getX() == 0 && tileEntry.getKey().getY() == 0) {
				spriteBatch.setColor(1, 0, 0, 1);
			} else {
				spriteBatch.setColor(Color.WHITE);
			}
			if (gridPosition.equals(tileEntry.getKey())) {
				spriteBatch.setColor(Color.BLUE);
			}
			spriteBatch.draw(outLine, center.x - outLine.originalWidth / 2,
					center.y - outLine.originalHeight / 2);
		}
		for (Entity entity : board.getEntities()) {
			if (entity.getPosition() == null)
				continue;
			Vector2 offset = offset(entity.getDirection(), entity.getPartialDistance());
			Vector2 renderPosition = center(entity.getPosition()).add(offset);
			spriteBatch.draw(entity.getTexture(), renderPosition.x, renderPosition.y);
		}
		spriteBatch.end();
	}

	// formula from http://www.redblobgames.com/grids/hexagons/#conversions
	private GridPosition pixelToGrid(int x, int y) {
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
		switch(direction) {
			case NORTH:
				directionVector = Y_AXIS_VEC.cpy();
				break;
			case NORTH_EAST:
				directionVector = Z_AXIS_VEC.cpy();
				break;
			case SOUTH_EAST:
				directionVector = X_AXIS_VEC.cpy();
				break;
			case SOUTH:
				directionVector = Y_AXIS_VEC.cpy().scl(-1);
				break;
			case SOUTH_WEST:
				directionVector = X_AXIS_VEC.cpy().scl(-1);
				break;
			case NORTH_WEST:
				directionVector = Z_AXIS_VEC.cpy().scl(-1);
				break;
			default:
				// this should never happen
				throw new AssertionError("Invalid direction");
		}
		return directionVector.scl(partialDistance);
	}
}
