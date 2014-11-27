package SpaceGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.TransformGroup;

public class Controller_Collision implements Observer{
	private ArrayList<Position> positions;
	private Stack<Stack<Ship>> killList;
	private Stack<Ship> currentKills;
	private int maxX;
	private int maxY;
	
	static Controller_Collision uniqueInstance;
	
	public static synchronized Controller_Collision getInstance(int maxX, int maxY) throws Exception_CM{
		if( uniqueInstance == null){
			uniqueInstance = new Controller_Collision(maxX, maxY);
			return uniqueInstance;
		}
		else{
			return uniqueInstance;
		}		
	}
	
	private Controller_Collision(int maxX, int maxY) {
		this.maxX = maxX;
		this.maxY = maxY;
		positions = new ArrayList<Position>();
		killList = new Stack<Stack<Ship>>();
		currentKills = new Stack<Ship>();
		
		for(int i = 0; i < maxX; i++){
			for(int j = 0; j < maxY; j++){
				Position p = new Position(i, j, 0);
				this.positions.add(p);
			}
		}
	}
	
	// Return a new RandomPosition
	public Position getRandomPosition() {
		Random rx = new Random();
		Random ry = new Random();
		Position p = this.getPosition(rx.nextInt(this.maxX -1) + 1, ry.nextInt(this.maxY - 1) + 1, 0);
		return p;
	}
	
	// Update the position of a ship
	@Override
	public void update(Ship sh) {
		// Clear the ships stored at each position
		for(Position p: this.positions){
			if(p.getShips().contains(sh)) p.getShips().remove(sh);
		}
		Position p = this.getPosition(sh.getX(), sh.getY(), sh.getZ());
		p.addShip(sh);
	}

	// return a position if it exists otherwise create a new one
	public Position getPosition(int x, int y, int z){
		for(Position p: this.positions){
			if(p.getX() == x && p.getY() == y && p.getZ() == z){
				return p;
			}
		}
		Position px = new Position(x, y, z);
		this.positions.add(px);
		return px;
	}
	
	public void resolveCollisions(Controller_Move mc, TransformGroup bigGroup) throws Exception_MS {
		ShipMother m = (ShipMother) mc.getShipByName("MotherShip");
		Position p = getPosition(m.getX(), m.getY(), m.getZ());
		
		if(p.getShips().size() == 1) return;
		else if(p.getShips().size() == 2){
			System.out.println("Collision - MotherShip wins!");
			for(Ship s: p.getShips()) if(!s.getName().equals("MotherShip")) currentKills.push(s);
		}
		else if(p.getShips().size() == 3){
			if(m.isAttacking()){
				for(Ship s: p.getShips()) if(!s.getName().equals("MotherShip")) currentKills.add(s);
				System.out.println("Collision - MotherShip wins!");
			}
			else{
				System.out.println("Collision - MotherShip loses!");
				m.setAlive(false);
			}
		}
		else m.setAlive(false);
		
		for(Ship s: currentKills) {
			p.removeShip(s);
			mc.removeShip(s);
			BranchGroup thisBranch = (BranchGroup) s.getShape().getMesh().getParent().getParent();
			bigGroup.removeChild(thisBranch);
		}
		killList.push(currentKills);
	}
	
	public void undoDeaths(Controller_Move mc){
		/*
		 * Pops the latest round of deaths from the stack and reverses them
		 */
		if(killList.isEmpty()) return;
		Stack<Ship> kills = killList.pop();
		while(!kills.isEmpty()){
			Ship sh = kills.pop();
			Position p = getPosition(sh.getX(), sh.getY(), sh.getZ());
			mc.addShip(sh);
			p.addShip(sh);
		}
	}
	
	public ArrayList<Position> getPositions(){
		return this.positions;
	}
}
