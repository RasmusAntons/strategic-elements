package net.frozenbit.strategicelements.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.GridPosition;

public abstract class Entity implements Disposable {
	private static final float BASE_SPEED = 2;

	private Board board;
	protected TextureAtlas atlas;
	private GridPosition position;
	private GridPosition.Direction direction;
	private boolean moving;
	private float speed;
	private float partialDistance;

	public Entity(Board board) {
		this.board = board;
		board.addEntity(this);
		direction = GridPosition.Direction.NORTH;
		moving = false;
		speed = BASE_SPEED;
		partialDistance = 0;
	}

	public GridPosition getPosition() {
		return position;
	}

	public void setPosition(GridPosition position) {
		this.position = position;
	}

	public void move(GridPosition.Direction direction) {
		moving = true;
		this.direction = direction;
	}

	public void onTick(float delta) {
		if (isMoving()) {
			float distance = speed * delta;
			if(getPartialDistance() + distance >= 1) {
				//setPartialDistance(getPartialDistance() + distance - 1); // more steps
				setPartialDistance(0);
				moving = false;
				setPosition(getPosition().getNeighbor(getDirection()));
			} else {
				setPartialDistance(getPartialDistance() + distance);
			}
		}
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

	public boolean isMoving() {
		return moving;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
}
