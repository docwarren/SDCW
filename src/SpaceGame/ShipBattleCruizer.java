package SpaceGame;

public class ShipBattleCruizer extends Ship {

	public ShipBattleCruizer(CollisionController cm, Position pos, float s) {
		super(cm, pos, s);
		this.setName("BattleCruizer");
		this.setShape(new ShipShape_Enemy());
	}
}
