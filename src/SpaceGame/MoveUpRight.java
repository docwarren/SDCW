package SpaceGame;

public class MoveUpRight extends Move {
	
	public MoveUpRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("DL");
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(500);
			this.getShip().move("UR");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
