package SpaceGame;

import java.util.ArrayList;

public class Position {
	private int x;
	private int y;
	private ArrayList<Ship> ships;
	
	public Position(int x, int y){
		this.setX(x);
		this.setY(y);
		this.ships = new ArrayList<Ship>();
	}
	
	public void addShip(Ship sh){
		this.ships.add(sh);
	}
	
	public void removeShip(Ship sh){
		this.ships.remove(sh);
	}
	
	public String toString(){
		return this.x + ": " + this.y;
	}
	//========================================Getters and setters======================
	
	public int getX() {
		return x;
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
