package SpaceGame;

public class MoveUpLeft extends Move {
	
	public MoveUpLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void move() {
		this.getShip().move("UL");
	}

	@Override
	public void undo() {
		this.getShip().move("DR");
	}

}
