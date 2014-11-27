package SpaceGame;

public class MoveDownLeft extends Move {
	
	public MoveDownLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("UR");
	}
	
	@Override
	public void run() {
		this.getShip().move("DL");
	}
}
