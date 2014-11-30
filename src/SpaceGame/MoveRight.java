package SpaceGame;

import entities.Ship;

public class MoveRight extends Move {

	public MoveRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("L");
	}
	
	@Override
	public void move() {
//		this.getShip().move("R");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getShip().move("R");
	}
}
