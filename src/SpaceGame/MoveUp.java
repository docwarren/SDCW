package SpaceGame;

public class MoveUp extends Move {

	public MoveUp(Ship sh) {
		super(sh);
	}

	@Override
	public void move() {
		this.getShip().move("U");
	}
	
	public void undo(){
		this.getShip().move("D");
	}
}
