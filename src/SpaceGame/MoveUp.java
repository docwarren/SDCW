package SpaceGame;

public class MoveUp extends Move {

	public MoveUp(Ship sh) {
		super(sh);
	}
	
	public void undo(){
		this.getShip().move("D");
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(350);
			this.getShip().move("U");
		} 
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
