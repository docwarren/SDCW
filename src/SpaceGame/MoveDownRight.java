package SpaceGame;

public class MoveDownRight extends Move {
	
	public MoveDownRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("UL");
	}
	
	@Override
	public void run() {
		this.getShip().move("DR");
	}
}
