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
		pushTurn(turn);
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
//	public void resolveCollisions(Text3D banner) throws Exception_MS {
//		// Check all the positions for the mothership.
//		// If she is on that square then either kill the enemies or the mothership
//		for(Position p: this.positions){
//			// Check for other ships on the same square
//			if(p.getShips().size() < 2) {
//				banner.setString(player.getX() + "," + player.getY());
//				return;
//			}		
//			else if(p.getShips().size() == 2 && p.hasPlayer()){
//				banner.setString("Enemy Killed!");
//				for(Ship s: p.getShips()) if(!s.getName().equals("MotherShip")){
//					turn.addKilledShip(s);
//					s.setAlive(false);
//					s.notifyObservers();
//				}
//			}
//			else if(p.getShips().size() == 3 && p.hasPlayer()){
//				if(player.isAttacking()){
//					banner.setString("2 Enemies Killed!");
//					for(Ship s: p.getShips()) if(!s.getName().equals("MotherShip")){
//						turn.addKilledShip(s);
//						s.setAlive(false);
//						s.notifyObservers();
//					}
//				}
//				else{
//					banner.setString("GAME OVER!");
//					player.setAlive(false);
//					player.notifyObservers();
//				}
//			}
//			else player.setAlive(false);
//		}
//		
//		// Reposition the ships so that they don't overlap
//		for(Position pos: positions){
//			for(int i = 0; i < pos.getShips().size(); i++){
//				pos.getShips().get(i).setZ(i);
//			}
//		}
//	}
	
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
