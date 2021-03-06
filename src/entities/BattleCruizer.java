package entities;

import graphics.ShipShape_Cruizer;

import java.util.ArrayList;

import SpaceGame.UniverseBuilder;
import behaviours.EnemyDeath;
import behaviours.ShipFlight;

public class BattleCruizer extends Ship {

	public BattleCruizer(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleCruizer");
		// Strategy Pattern - set flight and death behaviours
		this.setDeathBeaviour(new EnemyDeath());
		this.setFlyBehaviour(new ShipFlight());
		// Set the 3D objects associated with the ship
		this.setShape(new ShipShape_Cruizer());
		brGroup = shipBranchGroup();
	}
}
