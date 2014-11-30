package commands;

import entities.Ship;

public class MoveUpLeft extends Move {
	
	public MoveUpLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("DR");
	}
	
	@Override
	public void move() {
//		this.getShip().move("UL");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getShip().move("UL");
	}
}
