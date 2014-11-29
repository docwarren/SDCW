package SpaceGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import javax.media.j3d.Text3D;

public class MoveController{
	private Stack<Turn> turns;		// Keep track of all turns
	private ArrayList<Position> positions;		// Keeps track of where all the ships are
	private Move_Factory mvFactory;		// Generates moves
	private UniverseBuilder universe;
	private static String[] SHIPTYPES = {"BattleCruizer", "BattleShooter", "BattleStar"};
	private Ship_Factory shFactory;
	private static float TF_SCALE = 3.6f;
	private static int MAXX = 4;
	private static int MAXY = 4;
	
	// Singleton ====================================== Constructor ==================================
	static MoveController uniqueInstance;
	
	private MoveController(UniverseBuilder universe){
		this.setUniverse(universe);
		this.turns = new Stack<Turn>();
		this.positions = new ArrayList<Position>();
		for(int i = 0; i < MAXX; i++){
			for(int j = 0; j < MAXY; j++){
				Position p = new Position(universe, i, j);
				this.positions.add(p);
			}
		}
		this.mvFactory  = new Move_Factory();
		this.shFactory = new Ship_Factory(universe, getPositions(), TF_SCALE);
	}
	
	public static synchronized MoveController getInstance(UniverseBuilder universe) throws Exception_MC{
		if( uniqueInstance == null){
			uniqueInstance = new MoveController(universe);
			return uniqueInstance;
		}
		else{
			return uniqueInstance;
		}		
	}
	
	// ==============================================Class Methods====================================
	// Generate a random move for a ship
	public String randomMove(Ship sh){
		ArrayList<String> possibleMoves = new ArrayList<String>(Arrays.asList("U", "D", "R", "L", "UL", "UR", "DR", "DL"));
		if(sh.getX() == 0) {
			possibleMoves.remove("L");
			possibleMoves.remove("UL");
			possibleMoves.remove("DL");
		}
		else if(sh.getX() == MAXX - 1) {
			possibleMoves.remove("R");
			possibleMoves.remove("UR");
			possibleMoves.remove("DR");
		}
		if(sh.getY() == 0) {
			possibleMoves.remove("U");
			possibleMoves.remove("UR");
			possibleMoves.remove("UL");
		}
		else if(sh.getY() == MAXY - 1) {
			possibleMoves.remove("D");
			possibleMoves.remove("DL");
			possibleMoves.remove("DR");
		}
		
		Random rm = new Random();
		String move = possibleMoves.get(rm.nextInt(possibleMoves.size()));
		return move;
	}
	
	public Turn makeNewTurn() {
		/*
		 * Generates all the moves for all the ships		
		 */
		Turn turn = new Turn();
		// For each ship create a new move
		for(Position pos: this.positions){
			for(Ship sh: pos.getShips()){
				String moveType = randomMove(sh);				// Randomly generate a move type
				Move mv = mvFactory.createMove(sh, moveType);	// Create the appropriate move accordingly
				turn.addMove(mv);								// Add the moves to the list of moves to be executed this turn
			}
		}
		
		// Randomly create a new ship;
		Random r = new Random();
		if(r.nextInt(3) == 0){
			String shipType = SHIPTYPES[r.nextInt(SHIPTYPES.length)];
			Ship newShip = null;
			try {
				newShip = shFactory.createShip(shipType);
			} catch (Exception_MC | Exception_MS e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Notifies all the positions where this ship is and the Universe about the ship position and alive state
			newShip.notifyObservers();
			// Ensure that we record the new ship in the turn object
			turn.newShip(newShip);
		}
		
		return turn;
	}
	
	public void executeTurn(Turn turn) {
		/*
		 * Executes each move on the stack
		 */
		for(Move mv: turn.getMoves()){
			mv.move();
		}
		try {
			resolveFights(turn);
		} catch (Exception_MC | Exception_MS e) {
			e.printStackTrace();
		}
		
		
		pushTurn(turn);
		
		// Modify the z position based on the number of ships on each position.
		for(Position p: this.positions){
			System.out.print(p.getX() + "," + p.getY() + ": ");
			for(int i = 0; i < p.getShips().size(); i++){
				System.out.print(p.getShips().get(i).getName() + "; ");
				p.getShips().get(i).setZ(i);
				p.getShips().get(i).updateShape();
			}
			System.out.print("\n");
		}
		System.out.println("-----------------------------------------------------------------------------------");
	}
	
	private void resolveFights(Turn turn) throws Exception_MC, Exception_MS{
		// TODO Auto-generated method stub
		MotherShip player;
		player = (MotherShip) shFactory.createShip("MotherShip");
		Position p = getPosition(player.getX(), player.getY());
		int size = p.getShips().size();
		int result;
		if(size == 1) return;
		else if(size == 2){
			System.out.println("Mothership wins");
			for(Ship sh: p.getShips()){
				if(sh != player) result = sh.die();
				// Ensure that we record the death in the turn object
				turn.addKilledShip(sh);
			}
		}
		else if(size == 3 && player.isAttacking()){
			System.out.println("twice as nice");
			for(Ship sh: p.getShips()){
				if(sh != player) result = sh.die();
				// Ensure that we record the death in the turn object
				turn.addKilledShip(sh);
			}
		}
		else{
			result = player.die();
			System.out.println("GAME OVER");
		}
	}

	public void undoMove() {
		/*
		 * Undo the last turn
		 */
		if(!turns.isEmpty()){
			// Get the last turn
			Turn turn = popTurn();
			
			// Destroy any ships that were created by the turn
			if(turn.getNewShip() != null){
				System.out.println("Ship created here");
				Ship ship = turn.getNewShip();
				ship.setAlive(false);
				ship.notifyObservers();
			}
			// Recreate any ships killed by the turn
			for(Ship sh: turn.getKilledShips()){
				System.out.println("Ship killed here");
				sh.setAlive(true);
				sh.notifyObservers();
			}
			// Undo any moves those ships made
			for(Move mv: turn.getMoves()){
				if(shipExists(mv.getShip())) mv.undo();
			}
		}
	}
	
	public Ship getShipByName(String name){
		/*
		 * Only used for getting the mothership really
		 */
		for(Position pos: this.positions){
			for(Ship sh: pos.getShips()){
				if(sh.getName() == name) return sh;
			}
		}
		
		return null;
	}

	//================================================Collision Management====================================
	public Position getPosition(int x, int y){
		for(Position pos: this.positions){
			if(pos.getX() == x && pos.getY() == y){
				return pos;
			}
		}
		return null;
	}
	
	//================================================List modifiers===================================
	// Push a turn onto the stack
	public void pushTurn(Turn turn){
		this.turns.push(turn);
	}
	
	// Pop a turn from the stack
	public Turn popTurn(){
		return this.turns.pop();
	}
	
	public boolean shipExists(Ship ship){
		for(Position p: this.positions){
			if(p.getShips().contains(ship)) return true;
		}
		return false;
	}
	
	//===================================Getters and setters============================================
	public ArrayList<Position> getPositions() {
		return positions;
	}

	public UniverseBuilder getUniverse() {
		return universe;
	}

	public void setUniverse(UniverseBuilder universe) {
		this.universe = universe;
	}
	
	public float getScale(){
		return TF_SCALE;
	}
	
	public Ship_Factory getShipFactory(){
		return this.shFactory;
	}
	
	public int getMaxX(){
		return MAXX;
	}
	
	public int getMaxY(){
		return MAXY;
	}
}
