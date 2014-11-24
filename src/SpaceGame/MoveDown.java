package SpaceGame;

public class MoveDown extends Move {

	public MoveDown(Ship sh) {
		super(sh);
	}

	@Override
	public void move() {
		this.getShip().move("D");
	}

	@Override
	public void undo() {
		this.getShip().move("U");
	}

}
