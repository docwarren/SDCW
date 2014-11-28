package SpaceGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class Controller_Move{
	private Stack<Stack<Move>> turns;		// Keep track of all turn
	private Stack<Move> turn;				// Keep track of the current turn
	private ArrayList<Ship> ships;
	private int maxX;
	private int maxY;
	
	// Singleton ====================================== Constructor ==================================
	static Controller_Move uniqueInstance;
	
	private Controller_Move(int maxX, int maxY){
		this.maxX = maxX;
		this.maxY = maxY;
		this.turns = new Stack<Stack<Move>>();
		this.ships = new ArrayList<Ship>();
		this.turn = new Stack<Move>();
	}
	
	public static synchronized Controller_Move getInstance(int maxX, int maxY) throws Exception_MC{
		if( uniqueInstance == null){
			uniqueInstance = new Controller_Move(maxX, maxY);
			return uniqueInstance;
		}
		else{
			return uniqueInstance;
		}		
	}
	
	// =======================================================Class Methods====================================
	// Generate a random move for a ship
	public String randomMove(Ship sh){
		ArrayList<String> possibleMoves = new ArrayList<String>(Arrays.asList("U", "D", "R", "L", "UL", "UR", "DR", "DL"));
		if(sh.getX() == 0) {
			possibleMoves.remove("L");
			possibleMoves.remove("UL");
			possibleMoves.remove("DL");
		}
		else if(sh.getX() == maxX - 1) {
			possibleMoves.remove("R");
			possibleMoves.remove("UR");
			possibleMoves.remove("DR");
		}
		if(sh.getY() == 0) {
			possibleMoves.remove("U");
			possibleMoves.remove("UR");
			possibleMoves.remove("UL");
		}
		else if(sh.getY() == maxY - 1) {
			possibleMoves.remove("D");
			possibleMoves.remove("DL");
			possibleMoves.remove("DR");
		}
		
		Random rm = new Random();
		String move = possibleMoves.get(rm.nextInt(possibleMoves.size()));
		return move;
	}
	
	public void executeTurn() {
		Stack<Move> copyTurn = new Stack<Move>();
		for(Move mv: this.turn){
			mv.move();
			copyTurn.push(mv);
		}
		pushTurn(copyTurn);
		this.turn.clear();
	}
	
	public void undoMove() {
		Stack<Move> turn = popTurn();
		while(!turn.isEmpty()){
			Move mv = turn.pop();
			mv.undo();
		}
	}
	
	public Ship getShipByName(String name){
		for(Ship sh: this.ships){
			if(sh.getName() == name) return sh;
		}
		return null;
	}

	//================================================List modifiers===================================
	// Push a turn onto the stack
	public void pushTurn(Stack<Move> turn){
		this.turns.push(turn);
	}
	
	// Pop a turn from the stack
	public Stack<Move> popTurn(){
		if(turns.isEmpty()) return new Stack<Move>();
		return this.turns.pop();
	}
	
	// Add a ship to the game
	public void addShip(Ship sh){
		this.ships.add(sh);
	}
	
	// Remove a ship from the game
	public void removeShip(Ship sh){
		this.ships.remove(sh);
	}
	
	//===================================Getters and setters============================================
	public Stack<Stack<Move>> getTurns() {
		return turns;
	}

	public void setTurns(Stack<Stack<Move>> turns) {
		this.turns = turns;
	}

	public Stack<Move> getCurrentTurn() {
		return turn;
	}

	public void setCurrentTurn(Stack<Move> turn) {
		this.turn = turn;
	}

	public int getMaxX() {
		return maxX;
	}

	public void setMaxX(int maxX) {
		this.maxX = maxX;
	}

	public int getMaxY() {
		return maxY;
	}

	public void setMaxY(int maxY) {
		this.maxY = maxY;
	}
	
	public ArrayList<Ship> getShips(){
		return this.ships;
	}
}