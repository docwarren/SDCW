package SpaceGame;

public class MoveDownRight extends Move {
	
	public MoveDownRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("UL");
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(350);
			this.getShip().move("DR");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
}
