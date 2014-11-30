package entities;

import graphics.ShipShape_Enemy;

import java.util.ArrayList;

import SpaceGame.UniverseBuilder;

public class BattleShooter extends Ship {

	public BattleShooter(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleShooter");
		this.setShape(new ShipShape_Enemy());
		brGroup = shipBranchGroup();
	}
}
