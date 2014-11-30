package commands;

import entities.Ship;

public class MoveDown extends Move {

	public MoveDown(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("U");
	}

	@Override
	public void move() {
//		this.getShip().move("D");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getShip().move("D");
	}
}
