package entities;

import graphics.ShipShape_Passive;
import graphics.ShipShape_Player;

import java.util.ArrayList;

import behaviours.PlayerDeath;
import behaviours.PlayerFlight;
import SpaceGame.Exception_MS;
import SpaceGame.UniverseBuilder;

public class MotherShip extends Ship {
	private Boolean attackMode;

	// Singleton constructor
	static MotherShip uniqueInstance;
	
	private MotherShip(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) throws Exception_MS {
		super(universe, watchers, x, y, s);
		this.attackMode = true;
		this.setName("MotherShip");
		
		// Set behaviours
		this.setDeathBeaviour(new PlayerDeath());
		this.setFlyBehaviour(new PlayerFlight());
		this.setShape(new ShipShape_Player());
		
		brGroup = shipBranchGroup();
	}
	
	public static synchronized MotherShip getInstance(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) throws Exception_MS{
		if( uniqueInstance == null){
			uniqueInstance = new MotherShip(universe, watchers, x, y, s);
			return uniqueInstance;
		}
		else{
			return uniqueInstance;
		}
	}
	
	public void switchMode(){
		// Change the mode
		this.attackMode = !this.attackMode;
		// Change the mesh we use according to the mode
		if(isAttacking()) this.setShape(new ShipShape_Player());
		else this.setShape(new ShipShape_Passive());
	}
	
	public Boolean isAttacking(){
		return this.attackMode;
	}
}
