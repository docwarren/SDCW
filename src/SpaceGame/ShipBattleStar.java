package SpaceGame;

public class ShipBattleStar extends Ship {

	public ShipBattleStar(Controller_Collision cm, Position pos, float s) {
		super(cm, pos, s);
		this.setName("BattleStar");
		this.setShape(new ShipShape_Enemy());
	}
}
