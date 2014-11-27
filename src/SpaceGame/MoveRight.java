package SpaceGame;

public class MoveRight extends Move {

	public MoveRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("L");
	}
	
	@Override
	public void run() {
		this.getShip().move("R");
	}
}
