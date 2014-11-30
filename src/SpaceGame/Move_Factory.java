package SpaceGame;

import commands.Move;
import commands.MoveDown;
import commands.MoveDownLeft;
import commands.MoveDownRight;
import commands.MoveLeft;
import commands.MoveRight;
import commands.MoveUp;
import commands.MoveUpLeft;
import commands.MoveUpRight;
import entities.Ship;

public class Move_Factory {
	
	public Move_Factory() {
	}

	public Move createMove(Ship sh, String type){
		if(type.equals("U")) return new MoveUp(sh);
		else if(type.equals("D")) return new MoveDown(sh);
		else if(type.equals("L")) return new MoveLeft(sh);
		else if(type.equals("R")) return new MoveRight(sh);
		else if(type.equals("UR")) return new MoveUpRight(sh);
		else if(type.equals("DR")) return new MoveDownRight(sh);
		else if(type.equals("UL")) return new MoveUpLeft(sh);
		else return new MoveDownLeft(sh);
	}
}
