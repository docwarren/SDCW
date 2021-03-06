package entities;

import java.util.ArrayList;

import SpaceGame.UniverseBuilder;

public class Position implements Observer{
	private int x;
	private int y;
	private ArrayList<Ship> ships;
	UniverseBuilder universe;
	private boolean player;
	
	public Position(UniverseBuilder universe, int x, int y){
		this.universe = universe;
		this.setX(x);
		this.setY(y);
		this.ships = new ArrayList<Ship>();
	}
	
	@Override
	public void update(Ship ship) {
		if(ships.contains(ship)){
			ships.remove(ship);
			if(ship.getName().equals("MotherShip")) player = false;
		}
		else if(ship.getX() == x && ship.getY() == y && ship.isAlive()){
			ships.add(ship);
			if(ship.getName().equals("MotherShip")){
				player = true;
				fight();
			}
		}
	}

	//========================================List Modifiers
	public void fight(){
		System.out.println(ships.toString());
	}
	
	public void addShip(Ship sh){
		this.ships.add(sh);
	}
	
	public void removeShip(Ship sh){
		this.ships.remove(sh);
	}
	//========================================Getters and setters======================
	
	public int getX() {
		return x;
	}
	
	public boolean hasPlayer() {
		return this.player;
	}

	public void setHasPlayer(boolean player) {
		this.player = player;
	}

	public ArrayList<Ship> getShips() {
		return ships;
	}

	public void setShips(ArrayList<Ship> ships) {
		this.ships = ships;
	}

	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
