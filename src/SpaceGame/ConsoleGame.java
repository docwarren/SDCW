package SpaceGame;
import java.util.Random;
import java.util.Scanner;

public class ConsoleGame {
	private static Controller_Move mc;
	private static Controller_Collision cm;
	private static ShipMother player;
	private static Factory_Ship shFactory;
	private static Factory_Move mvFactory;
	
	public ConsoleGame(){}
	
	public static void main(String[] args) throws Exception_MC, Exception_MS, Exception_CM{
		mc = Controller_Move.getInstance(4, 4);
		cm = Controller_Collision.getInstance(4, 4);
		
		player = ShipMother.getInstance(cm, 0);		// Create the mothership
		mc.addShip(player);							// Add the mothership to the game
		player.notifyObservers();					// Update the collision detector about the position of the new ship
		
		shFactory = new Factory_Ship(cm, 0);	
		mvFactory = new Factory_Move();
		String[] shipTypes = {"BattleCruizer", "BattleShooter", "BattleStar"};
		
		// Print out all the positions and what ships are at that position
		printPositions();
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		// Until the player is dead or the input is "q"
		while(player.isAlive()){
			// Take input
			System.out.println("Enter next move");
			String input = in.next();
			
			if(input.equals("q")) break;
			else if(input.equals("m")){
				// Loop through all the positions
				for(Position p: cm.getPositions()){
					// For each ship create a new move
					for(Ship sh: p.getShips()){
						String moveType = mc.randomMove(sh);			// Randomly generate a move type
						Move mv = mvFactory.createMove(sh, moveType);	// Create the appropriate move accordingly
						mc.getCurrentTurn().add(mv);					// Add the moves to the list of moves to be executed this turn
					}
				}
				
				// Randomly create a new ship;
				Random r = new Random();
				int x = r.nextInt(3);
				Ship e = null;
				if(x == 0){
					e = shFactory.createShip(shipTypes[r.nextInt(shipTypes.length)]);
					mc.addShip(e);			// Add the new ship to the game
					e.notifyObservers();	// Notify the collision manager about the new ship
				}
				
				mc.executeTurn();
				//cm.resolveCollisions(mc, null);
				printPositions();
			}
			else if(input.equals("s")) {
				player.switchMode();
				if(player.isAttacking())System.out.println("Attack mode...");
				else System.out.println("Passive mode...");
			}
			else if(input.equals("u")) {
				cm.undoDeaths(mc);
				mc.undoMove();
				printPositions();
			}
		}
		System.out.println("GAME OVER!!!");
	}
	
	private static void printPositions() {
		for(Position p: cm.getPositions()){
			for(Ship ship: p.getShips()){
				System.out.println(ship.getName() + ": " + p.getX() + "," + p.getY());
			}
		}
	}
}
