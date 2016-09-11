package net.frozenbit.strategicelements.entities;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.GridPosition;

import java.util.List;
import java.util.Queue;

public class Entity implements Disposable {
	public static final int MAX_LEVEL = 3;
	private static final int BASE_RANGE = 3;
	private static final float BASE_SPEED = 2;
	private final TextureAtlas.AtlasRegion texture;
	private final Type type;
	protected TextureAtlas atlas;
	private Board board;
	private GridPosition position;
	private GridPosition renderPosition;
	private GridPosition.Direction direction;
	private Queue<GridPosition.Direction> path;
	private boolean enemy;
	private boolean moving;
	private int range;
	private float speed;
	private float partialDistance;
	private int level = 1;

	public Entity(Type type, GridPosition position, Board board, TextureAtlas atlas) {
		this.type = type;
		this.board = board;
		this.position = position;
		renderPosition = position;
		texture = atlas.findRegion("entity_" + type.name().toLowerCase());
		enemy = false;
		direction = GridPosition.Direction.NORTH;
		moving = false;
		range = BASE_RANGE;
		speed = BASE_SPEED;
		partialDistance = 0;
		board.addEntity(this);
	}

	public Type getType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		if (level < 1 || level > MAX_LEVEL) {
			throw new IllegalArgumentException("invalid level");
		}
		this.level = level;
	}

	public GridPosition getPosition() {
		return position;
	}

	public void setPosition(GridPosition position) {
		this.position = position;
		renderPosition = position;
		moving = false;
		path.clear();
	}

	public GridPosition getRenderPosition() {
		return renderPosition;
	}

	public void move(GridPosition destination, Queue<GridPosition.Direction> path) {
		position = destination;
		moving = true;
		this.path = path;
		direction = path.poll();
	}

	public void onTick(float delta) {
		if (isMoving()) {
			float distance = speed * delta;
			if(getPartialDistance() + distance >= 1) {
				if (!path.isEmpty()) {
					partialDistance = partialDistance + distance - 1;
				} else {
					partialDistance = 0;
					moving = false;

				}
				renderPosition = renderPosition.getNeighbor(direction);
			} else {
				partialDistance = partialDistance + distance;
				renderPosition = position;
			}
		}
	}

	public void dispose() {
		board.removeEntity(this);
	}

	public TextureRegion getTexture() {
		return texture;
	}

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

	public int getRange() {
		return range;
	}

	public boolean isEnemy() {
		return enemy;
	}

	public void setEnemy(boolean enemy) {
		this.enemy = enemy;
	}

	public enum Type {
		FIRE, WATER, EARTH
	}
}
