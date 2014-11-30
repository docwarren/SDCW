package entities;

import graphics.ShipShape_Enemy;

import java.util.ArrayList;

import SpaceGame.UniverseBuilder;

public class BattleStar extends Ship {

	public BattleStar(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleStar");
		this.setShape(new ShipShape_Enemy());
		brGroup = shipBranchGroup();
	}
}
