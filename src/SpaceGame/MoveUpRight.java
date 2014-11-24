package SpaceGame;

public class MoveUpRight extends Move {
	
	public MoveUpRight(Ship sh) {
		super(sh);
	}

	@Override
	public void move() {
		this.getShip().move("UR");
	}

	@Override
	public void undo() {
		this.getShip().move("DL");
	}

}
