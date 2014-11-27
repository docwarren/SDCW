package SpaceGame;

public class MoveDown extends Move {

	public MoveDown(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("U");
	}

	@Override
	public void run() {
		this.getShip().move("D");
		
	}

}
