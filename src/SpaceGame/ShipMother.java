package SpaceGame;

import javax.media.j3d.TransformGroup;

public class ShipMother extends Ship {
	private Boolean attackMode;
	
	// Singleton constructor
	static ShipMother uniqueInstance;
	
	private ShipMother(CollisionController cm, Position pos, float s) {
		super(cm, pos, s);
		this.attackMode = true;
		this.setName("MotherShip");
		this.setAlive(true);
		this.setShape(new ShipShape_Player());
	}
	
	public static synchronized ShipMother getInstance(CollisionController cm, float s) throws Exception_MS{
		if( uniqueInstance == null){
			uniqueInstance = new ShipMother(cm, cm.getRandomPosition(), s);
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
