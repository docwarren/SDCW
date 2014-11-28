package SpaceGame;

public class MoveLeft extends Move {

	public MoveLeft(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("R");
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(350);
			this.getShip().move("L");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
