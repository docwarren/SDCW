package SpaceGame;

public class ShipBattleShooter extends Ship {

	public ShipBattleShooter(Controller_Collision cm, Position pos, float s) {
		super(cm, pos, s);
		this.setName("BattleShooter");
		this.setShape(new ShipShape_Enemy());
	}
}
