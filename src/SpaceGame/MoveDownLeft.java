package SpaceGame;

public class MoveDownLeft extends Move {
	
	public MoveDownLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("UR");
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(500);
			this.getShip().move("DL");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
