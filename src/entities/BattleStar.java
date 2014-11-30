package entities;

import graphics.ShipShape_Enemy;

import java.util.ArrayList;
import behaviours.EnemyDeath;
import SpaceGame.UniverseBuilder;

public class BattleStar extends Ship {

	public BattleStar(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleStar");
		Ship.setDeathBeaviour(new EnemyDeath());
		this.setShape(new ShipShape_Enemy());
		brGroup = shipBranchGroup();
	}
}
