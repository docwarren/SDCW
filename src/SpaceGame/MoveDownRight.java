package SpaceGame;

public class MoveDownRight extends Move {
	
	public MoveDownRight(Ship sh) {
		super(sh);
	}

	@Override
	public void move() {
		this.getShip().move("DR");
	}

	@Override
	public void undo() {
		this.getShip().move("UL");
	}

}
