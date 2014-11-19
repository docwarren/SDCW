package SpaceGame;

public class MoveRight extends Move {

	public MoveRight(Ship sh) {
		super(sh);
	}

	@Override
	public void move() {
		this.getShip().move("right");
	}

	@Override
	public void undo() {
		this.getShip().move("left");
	}

}
