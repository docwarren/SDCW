package SpaceGame;

public class ShipFactory {
	private CollisionManager cm;
	
	public ShipFactory(CollisionManager cm) {
		this.cm = cm;
	}
	
	public Ship createShip(String shipType) throws MoveControllerException{
		Position p = cm.getPosition(0,0,0);
		if(shipType.equals("BattleCruizer")) return new BattleCruizer(cm, p);
		else if(shipType.equals("BattleStar")) return new BattleStar(cm, p);
		else return new BattleCruizer(cm, p);
	}
}
