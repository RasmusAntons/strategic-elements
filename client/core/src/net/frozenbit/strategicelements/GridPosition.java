package net.frozenbit.strategicelements;

import com.badlogic.gdx.math.Vector3;

/**
 * Represents a position in a hexagonal grid
 */
public class GridPosition {
	private int x, y;

	public GridPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static GridPosition fromCubic(Vector3 cubic) {
		int x = (int) cubic.x;
		int y = (int) cubic.z;
		return new GridPosition(x, y);
	}

	/**
	 * Rounds floating-point axial coordinates to the nearest integer axial coordinates
	 * Mostly <strike>stolen from</strike> <i>inspired by</i> this
	 * <a href="http://www.redblobgames.com/grids/hexagons/#conversions">helpful website</a>
	 *
	 * @param x floating point axial x coordinate
	 * @param y floating point axial y coordinate
	 * @return the nearest
	 */
	public static GridPosition axialRound(float x, float y) {
		return fromCubic(cubicRound(axialToCubic(x, y)));
	}

	// formula from http://www.redblobgames.com/grids/hexagons/#conversions
	private static Vector3 cubicRound(Vector3 pos) {
		int rx = Math.round(pos.x);
		int ry = Math.round(pos.y);
		int rz = Math.round(pos.z);

		float xDiff = Math.abs(rx - pos.x);
		float yDiff = Math.abs(ry - pos.y);
		float zDiff = Math.abs(rz - pos.z);

		if (xDiff > yDiff && xDiff > zDiff)
			rx = -ry - rz;
		else if (yDiff > zDiff)
			ry = -rx - rz;
		else
			rz = -rx - ry;

		return new Vector3(rx, ry, rz);
	}

	// formula from http://www.redblobgames.com/grids/hexagons/#conversions
	private static Vector3 axialToCubic(float q, float r) {
		return new Vector3(q, (-q - r), r);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public Vector3 toCubic() {
		float x = this.x;
		float z = this.y;
		float y = -x - z;
		return new Vector3(x, y, z);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		GridPosition that = (GridPosition) o;

		return x == that.x && y == that.y;
	}

	@Override
	public int hashCode() {
		int result = x;
		result = 31 * result + y;
		return result;
	}

	public GridPosition getNeighbor(Direction direction) {
		if (direction == null) {
			throw new IllegalArgumentException("direction must not be null");
		}
		int x, y;
		switch (direction) {
			case NORTH:
				x = this.x;
				y = this.y - 1;
				break;
			case NORTH_EAST:
				x = this.x + 1;
				y = this.y - 1;
				break;
			case SOUTH_EAST:
				x = this.x + 1;
				y = this.y;
				break;
			case SOUTH:
				x = this.x;
				y = this.y + 1;
				break;
			case SOUTH_WEST:
				x = this.x - 1;
				y = this.y + 1;
				break;
			case NORTH_WEST:
				x = this.x - 1;
				y = this.y;
				break;
			default:
				// this should never happen
				throw new AssertionError("Invalid direction");
		}
		return new GridPosition(x, y);
	}


	public enum Direction {
		NORTH, NORTH_EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, NORTH_WEST;

		public Direction invert() {
			switch (this) {
				case NORTH:
					return SOUTH;
				case NORTH_EAST:
					return SOUTH_WEST;
				case SOUTH_EAST:
					return NORTH_WEST;
				case SOUTH:
					return NORTH;
				case SOUTH_WEST:
					return NORTH_EAST;
				case NORTH_WEST:
					return SOUTH_EAST;
				default:
					throw new AssertionError("Invalid direction");
			}
		}
	}
}
