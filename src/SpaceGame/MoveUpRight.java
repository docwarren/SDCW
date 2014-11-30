package SpaceGame;

import entities.Ship;

public class MoveUpRight extends Move {
	
	public MoveUpRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("DL");
	}
	
	@Override
	public void move() {
//		this.getShip().move("UR");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getShip().move("UR");
	}
}