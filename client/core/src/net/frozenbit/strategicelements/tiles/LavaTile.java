package net.frozenbit.strategicelements.tiles;


public class LavaTile extends Tile {
	@Override
	public boolean canWalk() {
		return false;
	}

	@Override
	public String getName() {
		return "lava";
	}
}
