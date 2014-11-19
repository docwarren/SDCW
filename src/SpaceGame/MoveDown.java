package SpaceGame;

public class MoveDown extends Move {

	public MoveDown(Ship sh) {
		super(sh);
	}

	@Override
	public void move() {
		this.getShip().move("down");
	}

	@Override
	public void undo() {
		this.getShip().move("up");
	}

}
