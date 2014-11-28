package SpaceGame;

public class MoveUpLeft extends Move {
	
	public MoveUpLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("DR");
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(500);
			this.getShip().move("UL");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
