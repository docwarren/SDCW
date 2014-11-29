package SpaceGame;

import java.util.ArrayList;

public class Turn {
	private ArrayList<Move> moves;
	private Ship newShip;
	private ArrayList<Ship> killedShips;
	
	//==========================================Constructor================================================
	public Turn(){
		this.moves = new ArrayList<Move>();
		this.newShip = null;
		this.killedShips = new ArrayList<Ship>();
	}
	
	//==========================================Class Methods================================================
	
	
	//==========================================List Modifiers==============================================
	public void addMove(Move m){
		this.moves.add(m);
	}
	
	public void addKilledShip(Ship sh){
		this.killedShips.add(sh);
	}
	
	public void newShip(Ship ship){
		this.newShip = ship;
	}
	
	//==========================================Getters and SEtters==========================================
	public ArrayList<Move> getMoves() {
		return this.moves;
	}
	
	public void setMoves(ArrayList<Move> moves) {
		this.moves = moves;
	}
	
	public Ship getNewShip() {
		return this.newShip;
	}
	
	public ArrayList<Ship> getKilledShips() {
		return this.killedShips;
	}
	
	public void setKilledShips(ArrayList<Ship> killedShips) {
		this.killedShips = killedShips;
	}
}
