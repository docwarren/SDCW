package commands;

import entities.Ship;

public class MoveUp extends Move {

	public MoveUp(Ship sh) {
		super(sh);
	}
	
	@Override
	public void undo(){
		this.getShip().move("D");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getShip().move("U");
	}
}
