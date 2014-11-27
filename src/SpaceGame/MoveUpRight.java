package SpaceGame;

public class MoveUpRight extends Move {
	
	public MoveUpRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("DL");
	}
	
	@Override
	public void run() {
		this.getShip().move("UR");
	}
}
