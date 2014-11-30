package SpaceGame;

import entities.Ship;

public class MoveDownRight extends Move {
	
	public MoveDownRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("UL");
	}
	
	@Override
	public void move() {
//		this.getShip().move("DR");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getShip().move("DR");
	}
}
