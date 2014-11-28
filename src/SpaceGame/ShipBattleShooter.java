package SpaceGame;

public class ShipBattleShooter extends Ship {

	public ShipBattleShooter(CollisionController cm, Position pos, float s) {
		super(cm, pos, s);
		this.setName("BattleShooter");
		this.setShape(new ShipShape_Enemy());
	}
}
