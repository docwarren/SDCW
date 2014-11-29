package SpaceGame;

import java.util.ArrayList;

public class BattleStar extends Ship {

	public BattleStar(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleStar");
		this.setShape(new ShipShape_Enemy());
		brGroup = shipBranchGroup();
	}
}
