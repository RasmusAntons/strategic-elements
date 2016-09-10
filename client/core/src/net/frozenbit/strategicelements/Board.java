package net.frozenbit.strategicelements;

import net.frozenbit.strategicelements.tiles.GroundTile;
import net.frozenbit.strategicelements.tiles.Tile;
import net.frozenbit.strategicelements.tiles.VoidTile;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by mark on 10.09.16.
 * <p>
 * ⬣
 */
public class Board {
	private static final VoidTile VOID_TILE = new VoidTile();
	private Map<GridPosition, Tile> tiles;

	public Board() {
		this.tiles = new HashMap<>();
		tiles.put(new GridPosition(0, 0), new GroundTile());
		tiles.put(new GridPosition(0, -1), new GroundTile());
		tiles.put(new GridPosition(1, -1), new GroundTile());
		tiles.put(new GridPosition(1, 0), new GroundTile());
		tiles.put(new GridPosition(0, 1), new GroundTile());
		tiles.put(new GridPosition(-1, 1), new GroundTile());
		tiles.put(new GridPosition(-1, 0), new GroundTile());
	}

	public Tile getTile(GridPosition pos) {
		Tile tile = tiles.get(pos);
		return tile != null ? tile : VOID_TILE;
	}

	public Set<Map.Entry<GridPosition, Tile>> getTiles() {
		return tiles.entrySet();
	}

}
