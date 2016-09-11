package net.frozenbit.strategicelements.tiles;


public class GroundTile extends Tile {
	@Override
	public boolean canWalk() {
		return true;
	}

	@Override
	public String getName() {
		return "ground";
	}
}
