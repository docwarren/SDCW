package SpaceGame;

public class MoveFactory {
	
	public MoveFactory() {
	}

	public Move createMove(Ship sh, String type){
		if(type.equals("up")) return new MoveUp(sh);
		else if(type.equals("down")) return new MoveDown(sh);
		else if(type.equals("left")) return new MoveLeft(sh);
		else return new MoveRight(sh);
	}
}
