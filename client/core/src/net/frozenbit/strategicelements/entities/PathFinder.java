package net.frozenbit.strategicelements.entities;

import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.GridPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PathFinder {
	private Board board;

	public PathFinder(Board board) {
		this.board = board;
	}

	public Set<GridPosition> possibleDestinations(Entity entity) {
		Set<GridPosition> visited = new HashSet<>();
		GridPosition start = entity.getPosition();
		visited.add(start);
		List<List<GridPosition>> fringes = new ArrayList<>();
		List<GridPosition> startFringe = new ArrayList<>();
		startFringe.add(start);
		fringes.add(startFringe);

		for (int k = 1; k <= 3; ++k) {
			fringes.add(new ArrayList<GridPosition>());
			for (GridPosition cube : fringes.get(k - 1)) {
				for (GridPosition.Direction dir : GridPosition.Direction.values()) {
					GridPosition neighbor = cube.getNeighbor(dir);
					if (!visited.contains(neighbor) && board.getTile(neighbor).canWalk()) {
						visited.add(neighbor);
						fringes.get(k).add(neighbor);
					}
				}
			}
		}

		return visited;
	}

	public List<GridPosition.Direction> pathTo(Entity entity, GridPosition position) {
		return null;
	}
}
