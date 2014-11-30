package entities;

import graphics.ShipShape_Enemy;

import java.util.ArrayList;
import behaviours.EnemyDeath;
import SpaceGame.UniverseBuilder;

public class BattleCruizer extends Ship {

	public BattleCruizer(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleCruizer");
		Ship.setDeathBeaviour(new EnemyDeath());
		this.setShape(new ShipShape_Enemy());
		brGroup = shipBranchGroup();
	}
}
