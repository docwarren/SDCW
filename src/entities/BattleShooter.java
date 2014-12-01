package entities;

import graphics.ShipShape_Enemy;

import java.util.ArrayList;

import SpaceGame.UniverseBuilder;
import behaviours.EnemyDeath;
import behaviours.ShipFlight;

public class BattleShooter extends Ship {

	public BattleShooter(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleShooter");
		// Strategy Pattern - set flight and death behaviours
		this.setDeathBeaviour(new EnemyDeath());
		this.setFlyBehaviour(new ShipFlight());
		// Set the 3D objects associated with the ship
		this.setShape(new ShipShape_Enemy());
		brGroup = shipBranchGroup();
	}
}
