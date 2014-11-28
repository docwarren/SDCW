package SpaceGame;

public class MoveDown extends Move {

	public MoveDown(Ship sh) {
		super(sh);
	}

	@Override
	public void undo() {
		this.getShip().move("U");
	}

	@Override
	public void run() {
		try {
			Thread.sleep(350);
			this.getShip().move("D");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
