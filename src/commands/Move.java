package commands;

import entities.Ship;

public abstract class Move implements Runnable{
	private Ship ship;
	public abstract void undo();
	
	public Move(Ship sh){
		this.ship = sh;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
