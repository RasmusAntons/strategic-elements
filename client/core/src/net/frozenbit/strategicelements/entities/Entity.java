package net.frozenbit.strategicelements.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.GridPosition;

public abstract class Entity implements Disposable {
	private Board board;
	protected TextureAtlas atlas;
	private GridPosition position;
	private GridPosition.Direction direction;
	private float partialDistance;

	public Entity(Board board) {
		this.board = board;
		board.addEntity(this);
		direction = GridPosition.Direction.NORTH;
		partialDistance = 0;
	}

	public GridPosition getPosition() {
		return position;
	}

	public void setPosition(GridPosition position) {
		this.position = position;
	}

	public void dispose() {
		board.removeEntity(this);
	}

	public abstract TextureRegion getTexture();

	public GridPosition.Direction getDirection() {
		return direction;
	}

	public void setDirection(GridPosition.Direction direction) {
		this.direction = direction;
	}

	public float getPartialDistance() {
		return partialDistance;
	}

	public void setPartialDistance(float partialDistance) {
		this.partialDistance = partialDistance;
	}
}
