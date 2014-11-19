package SpaceGame;

public abstract class Move {
	private Ship ship;
	
	public abstract void move();
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
