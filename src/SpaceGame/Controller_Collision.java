package SpaceGame;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.TransformGroup;

import com.sun.j3d.utils.geometry.Text2D;

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
	
	public void resolveCollisions(Controller_Move mc, TransformGroup bigGroup, Text3D banner) throws Exception_MS {
		// Get the MotherShip object
		ShipMother m = (ShipMother) mc.getShipByName("MotherShip");
		// Get the position of the MotherShip
		Position p = getPosition(m.getX(), m.getY(), m.getZ());
		
		// Check for other ships on the same square
		if(p.getShips().size() == 1) {
			banner.setString("Space Wars!");
			return;
		}
		else if(p.getShips().size() == 2){
			banner.setString("Enemy Killed!");
			for(Ship s: p.getShips()) if(!s.getName().equals("MotherShip")) currentKills.push(s);
		}
		else if(p.getShips().size() == 3){
			if(m.isAttacking()){
				for(Ship s: p.getShips()) if(!s.getName().equals("MotherShip")) currentKills.add(s);
				banner.setString("Enemy Killed!");
			}
			else{
				banner.setString("GAME OVER!");
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
		
		// Reposition the ships so that they don't overlap
		for(Position pos: positions){
			for(int i = 0; i < pos.getShips().size(); i++){
				pos.getShips().get(i).setZ(i);
			}
		}
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
