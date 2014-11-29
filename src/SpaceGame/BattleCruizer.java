package SpaceGame;

import java.util.ArrayList;

public class BattleCruizer extends Ship {

	public BattleCruizer(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s) {
		super(universe, watchers, x, y, s);
		this.setName("BattleCruizer");
		this.setShape(new ShipShape_Enemy());
		brGroup = shipBranchGroup();
	}
}
