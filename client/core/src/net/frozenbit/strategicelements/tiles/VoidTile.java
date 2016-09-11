package net.frozenbit.strategicelements.tiles;


public class VoidTile extends Tile {
	@Override
	public boolean canWalk() {
		return false;
	}

	@Override
	public String getName() {
		return "void";
	}
}
