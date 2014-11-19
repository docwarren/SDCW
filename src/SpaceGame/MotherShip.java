package SpaceGame;

public class MotherShip extends Ship {
	private Boolean attackMode;
	
	// Singleton constructor
	static MotherShip uniqueInstance;
	
	private MotherShip(CollisionManager cm, Position pos) {
		super(cm, pos);
		this.attackMode = true;
		this.setName("MotherShip");
		this.setAlive(true);
	}
	
	public static synchronized MotherShip getInstance(CollisionManager cm) throws MotherShipException{
		if( uniqueInstance == null){
			uniqueInstance = new MotherShip(cm, cm.getRandomPosition());
			return uniqueInstance;
		}
		else{
			return uniqueInstance;
		}
	}
	
	public void switchMode(){
		this.attackMode = !this.attackMode;
	}
	
	public Boolean isAttacking(){
		return this.attackMode;
	}
}
