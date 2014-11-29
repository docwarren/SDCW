package SpaceGame;

public class MoveLeft extends Move {

	public MoveLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("R");
	}
	
	@Override
	public void move() {
		this.getShip().move("L");
	}
}
