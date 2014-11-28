package SpaceGame;

public class Factory_Ship {
	private CollisionController cm;
	private float scale;
	
	public Factory_Ship(CollisionController cm, float s) {
		this.cm = cm;
		this.scale = s;
	}
	
	public Ship createShip(String shipType) throws Exception_MC{
		Position p = cm.getPosition(0,0);
		if(shipType.equals("BattleCruizer")) return new ShipBattleCruizer(cm, p, scale);
		else if(shipType.equals("BattleStar")) return new ShipBattleStar(cm, p, scale);
		else return new ShipBattleCruizer(cm, p, scale);
	}
}
