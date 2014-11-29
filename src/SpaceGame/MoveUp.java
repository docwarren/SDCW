package SpaceGame;

public class MoveUp extends Move {

	public MoveUp(Ship sh) {
		super(sh);
	}
	
	public void undo(){
		this.getShip().move("D");
	}
	
	@Override
	public void move() {
		this.getShip().move("U");
	}
}
