package net.frozenbit.strategicelements.entities;

import net.frozenbit.strategicelements.Board;
import net.frozenbit.strategicelements.GridPosition;

import java.util.*;

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

		for (int k = 1; k <= entity.getRange(); ++k) {
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

	// inspired by http://www.peachpit.com/articles/article.aspx?p=101142
	public Queue<GridPosition.Direction> pathTo(Entity entity, GridPosition destination) {
		GridPosition startNode = entity.getPosition();
		Set<GridPosition> closedList = new HashSet<>();

		LinkedList<GridPosition> openList = new LinkedList<>();
		Map<GridPosition, GridPosition.Direction> pathParent = new HashMap<>();

		openList.add(startNode);
		while (!openList.isEmpty()) {
			GridPosition node = openList.removeFirst();
			if (node.equals(destination)) {
				return constructPath(pathParent, destination);
			} else {
				closedList.add(node);

				// add neighbors to the open list
				for (GridPosition.Direction direction : GridPosition.Direction.values()) {
					GridPosition neighborNode = node.getNeighbor(direction);
					if (!closedList.contains(neighborNode) &&
							!openList.contains(neighborNode)) {
						pathParent.put(neighborNode, direction.invert());
						openList.add(neighborNode);
					}
				}
			}
		}

		return null;
	}

	private Queue<GridPosition.Direction> constructPath(Map<GridPosition, GridPosition.Direction> pathParent, GridPosition goalNode) {
		Deque<GridPosition.Direction> path = new ArrayDeque<>();
		while (pathParent.containsKey(goalNode)) {
			GridPosition.Direction direction = pathParent.get(goalNode);
			goalNode = goalNode.getNeighbor(direction);
			path.addFirst(direction.invert());
		}
		return path;
	}

}
