package commands;

import entities.Ship;

public class MoveLeft extends Move {

	public MoveLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("R");
	}
	
	@Override
	public void move() {
//		this.getShip().move("L");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getShip().move("L");
	}
}
