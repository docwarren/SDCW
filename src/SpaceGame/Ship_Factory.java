package SpaceGame;

import java.util.ArrayList;
import java.util.Random;

import entities.BattleCruizer;
import entities.BattleShooter;
import entities.BattleStar;
import entities.MotherShip;
import entities.Position;
import entities.Ship;

public class Ship_Factory {
	private ArrayList<Position> positions;
	private float scale;
	private UniverseBuilder universe;
	
	public Ship_Factory(UniverseBuilder universe, ArrayList<Position> pos, float s) {
		this.positions = pos;
		this.scale = s;
		this.universe = universe;
	}
	
	public Ship createShip(String shipType) throws Exception_MC, Exception_MS{
		Random rx = new Random();
		Random ry = new Random();
		if(shipType.equals("MotherShip")) return MotherShip.getInstance(universe, positions, rx.nextInt(3), ry.nextInt(3), scale);
		else if(shipType.equals("BattleCruizer")) return new BattleCruizer(universe, positions, 0, 0, scale);
		else if(shipType.equals("BattleStar")) return new BattleStar(universe, positions, 0, 0, scale);
		else return new BattleShooter(universe, positions, 0, 0, scale);
	}
}
