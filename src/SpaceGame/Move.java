package SpaceGame;

public abstract class Move extends Thread{
	private Ship ship;
	public abstract void undo();
	public abstract void run();
	
	public Move(Ship sh){
		this.ship = sh;
	}
	
	public void move(){
		this.run();
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}
}
