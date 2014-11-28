package SpaceGame;

public class MoveRight extends Move {

	public MoveRight(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("L");
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(500);
			this.getShip().move("R");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
