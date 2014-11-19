package SpaceGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

public class MoveController{
	private Stack<Stack<Move>> turns;		// Keep track of all turn
	private Stack<Move> turn;				// Keep track of the current turn
	private ArrayList<Ship> ships;
	private int maxX;
	private int maxY;
	
	// Singleton ====================================== Constructor ==================================
	static MoveController uniqueInstance;
	
	private MoveController(int maxX, int maxY){
		this.maxX = maxX;
		this.maxY = maxY;
		this.turns = new Stack<Stack<Move>>();
		this.ships = new ArrayList<Ship>();
		this.turn = new Stack<Move>();
	}
	
	public static synchronized MoveController getInstance(int maxX, int maxY) throws MoveControllerException{
		if( uniqueInstance == null){
			uniqueInstance = new MoveController(maxX, maxY);
			return uniqueInstance;
		}
		else{
			return uniqueInstance;
		}		
	}
	
	// =======================================================Class Methods====================================
	// Generate a random move for a ship
	public String randomMove(Ship sh){
		ArrayList<String> possibleMoves = new ArrayList<String>(Arrays.asList("up", "down", "right", "left"));
		if(sh.getX() == 0) possibleMoves.remove("left");
		else if(sh.getX() == maxX - 1) possibleMoves.remove("right");
		if(sh.getY() == 0) possibleMoves.remove("up");
		else if(sh.getY() == maxY - 1) possibleMoves.remove("down");
		
		Random rm = new Random();
		String move = possibleMoves.get(rm.nextInt(possibleMoves.size()));
		System.out.println("Random move: " + move);
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
