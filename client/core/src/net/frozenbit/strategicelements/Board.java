package net.frozenbit.strategicelements;

import net.frozenbit.strategicelements.entities.Entity;
import net.frozenbit.strategicelements.tiles.GroundTile;
import net.frozenbit.strategicelements.tiles.Tile;
import net.frozenbit.strategicelements.tiles.VoidTile;

import java.util.*;

/**
 * Created by mark on 10.09.16.
 * <p>
 * ⬣
 */
public class Board {
	private static final VoidTile VOID_TILE = new VoidTile();
	private Map<GridPosition, Tile> tiles;
	private List<Entity> entities;

	public Board() {
		this.tiles = new HashMap<>();
		entities = new ArrayList<>();
		for (int i = -15; i < 15; ++i) {
			for (int j = -8; j < 8; ++j) {
				tiles.put(new GridPosition(i, j - ((i + (i > 0 ? 1 : 0)) / 2)), new GroundTile());
			}
		}
	}

	public Entity getEntityByPosition(GridPosition position) {
		for (Entity entity : entities) {
			if (entity.getPosition().equals(position)) {
				return entity;
			}
		}
		return null;
	}

	public boolean hasTile(GridPosition pos) {
		return tiles.containsKey(pos);
	}

	public Tile getTile(GridPosition pos) {
		Tile tile = tiles.get(pos);
		return tile != null ? tile : VOID_TILE;
	}

	public Set<Map.Entry<GridPosition, Tile>> getTiles() {
		return tiles.entrySet();
	}

	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	public void removeEntity(Entity entity) {
		entities.remove(entity);
	}

	public List<Entity> getEntities() {
		return entities;
	}

}
