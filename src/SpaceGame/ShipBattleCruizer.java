package SpaceGame;

public class ShipBattleCruizer extends Ship {

	public ShipBattleCruizer(Controller_Collision cm, Position pos, float s) {
		super(cm, pos, s);
		this.setName("BattleCruizer");
		this.setShape(new ShipShape_Enemy());
	}
}
