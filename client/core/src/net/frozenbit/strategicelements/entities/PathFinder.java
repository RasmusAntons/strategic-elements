package net.frozenbit.strategicelements.entities;

import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.GridPosition;

import java.util.List;
import java.util.Set;

public class PathFinder {
	private Board board;

	public PathFinder(Board board) {
		this.board = board;
	}

	public Set<GridPosition> possibleDestinations(Entity entity) {
		return null;
	}

	public List<GridPosition.Direction> pathTo(Entity entity, GridPosition position) {
		return null;
	}
}
