package SpaceGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import javax.media.j3d.Text3D;

import commands.Move;

import entities.MotherShip;
import entities.Position;
import entities.Ship;

public class MoveController{
	private Stack<Turn> turns;					// Keep track of all turns
	private ArrayList<Position> positions;		// Keeps track of where all the ships are
	private Move_Factory mvFactory;				// Generates moves
	private UniverseBuilder universe;			// Has methods for adding and removing objects to the scene
	private static String[] SHIPTYPES = {"BattleCruizer", "BattleShooter", "BattleStar"};
	private Ship_Factory shFactory;				// Generates ships
	private static float TF_SCALE = 3.6f;
	private static int MAXX = 4;
	private static int MAXY = 4;
	private Text3D banner;
	private int kills;
	private int moves;
	
	// Singleton ====================================== Constructor ==============================================
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
	
	// ==============================================Class Methods================================================
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
				e.printStackTrace();
			}
			// Notifies all the positions where this ship is and the Universe about the ship position and alive state
			newShip.notifyObservers();
			// Ensure that we record the new ship in the turn object
			turn.newShip(newShip);
		}
		
		return turn;
	}
	
	public void executeTurn(MotherShip player) {
		/*
		 * Executes each move on the stack
		 */
		moves++;			// Counting turns for displaying on the banner
		Turn turn = makeNewTurn();
		turn.getMoves().sort(new turnSort(player));
		for(Move mv: turn.getMoves()){
			mv.run();	// Used instead of start() to allow delays to be sequential rather than parallel
		}
		try {
			resolveCollisions(turn);
		} catch (Exception_MC | Exception_MS e) {
			e.printStackTrace();
		}
		
		pushTurn(turn);
		
		// Modify the z position based on the number of ships on each position.
		for(Position p: this.positions){
			System.out.print(p.getX() + "," + p.getY() + ": ");
			for(int i = 0; i < p.getShips().size(); i++){
				Ship ship = p.getShips().get(i);
				System.out.print(ship.getName() + "; ");
				ship.setZ(i);
				ship.getFlyBehaviour().fly(ship);
			}
			System.out.print("\n");
		}
		System.out.println("-----------------------------------------------------------------------------");
	}
	
	private void resolveCollisions(Turn turn) throws Exception_MC, Exception_MS{
		/*
		 * Resolve any conflict in a player square
		 */
		MotherShip player;
		player = (MotherShip) shFactory.createShip("MotherShip");
		Position p = getPosition(player.getX(), player.getY());
		int size = p.getShips().size();
		// If there is only one player on the square it is the player
		if(size == 1) {
			banner.setString("Kills:" + kills + "  Moves:" + moves);
			return;
		}
		// If there is only one enemy on the square MotherShip wins
		else if(size == 2){
			kills++;			// Counting kills for displaying on the banner
			System.out.println("Enemy Destroyed!");
			banner.setString("Enemy Destroyed!");
			for(int i = 0; i < p.getShips().size(); i++){
				if(p.getShips().get(i) != player) {
					turn.addKilledShip(p.getShips().get(i));		// Ensure that we record the death in the turn object
					p.getShips().get(i).die();
				}
			}
		}
		// If there are 2 enemies on the square MotherShip only wins if she is in attack mode
		else if(size == 3 && player.isAttacking()){
			kills += 2;			// Counting kills for displaying on the banner
			System.out.println("Twice as Nice!");
			banner.setString("Twice as Nice!");
			Ship x = null;
			Ship k = null;
			for(int i = 0; i < size; i++){
				
				if(p.getShips().get(i) != player) {
					turn.addKilledShip(p.getShips().get(i));		// Ensure that we record the death in the turn object
					if(x == null) x = p.getShips().get(i);
					else k = p.getShips().get(i);
				}
			}
			k.die();	// Have to do this because the array-list gets shorter as ships die 
			x.die();	// Could use a hash-map but there are only 2 to deal with
		}
		else{
			player.die();
			if(kills > 1 || kills == 0) banner.setString("GAME OVER: " + kills + " kills.");
			else banner.setString("GAME OVER: " + kills + " kill.");
			
		}
	}

	public void undoMove() {
		/*
		 * Undo the last turn
		 */
		if(!turns.isEmpty()){
			// Get the last turn
			Turn turn = popTurn();
			moves--;			// Counting moves for displaying on the banner
			banner.setString("Kills: " + kills + "  Moves: " + moves);
			
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
				kills--;
				banner.setString("Kills: " + kills + "  Moves: " + moves);
				sh.setAlive(true);
				sh.notifyObservers();
			}
			// Undo any moves those ships made
			for(Move mv: turn.getMoves()){
				if(shipExists(mv.getShip())) mv.undo();
			}
		}
		
		// Modify the z position based on the number of ships on each position. ( so they don't overlap )
		for(Position p: this.positions){
			System.out.print(p.getX() + "," + p.getY() + ": ");
			for(int i = 0; i < p.getShips().size(); i++){
				Ship ship = p.getShips().get(i);
				System.out.print(ship.getName() + "; ");
				ship.setZ(i);
				ship.getFlyBehaviour().fly(ship);
			}
			System.out.print("\n");
		}
		System.out.println("--------------------------------------------------------------------------------");
	}
	
	public Ship getShipByName(String name){
		/*
		 * Only used for getting the mother-ship really
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
		/*
		 * Given the x and y coordinates of a ship will return the position the ship is at
		 */
		for(Position pos: this.positions){
			if(pos.getX() == x && pos.getY() == y){
				return pos;
			}
		}
		return null;
	}
	
	//================================================List modifiers==========================================
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
	
	//===============================================Getters and setters======================================
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

	public Text3D getBanner() {
		return banner;
	}

	public void setBanner(Text3D banner) {
		this.banner = banner;
	}
}
