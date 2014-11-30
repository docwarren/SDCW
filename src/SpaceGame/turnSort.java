package SpaceGame;

import java.util.Comparator;

import commands.Move;

import entities.MotherShip;

public class turnSort implements Comparator<Move>{
	
	private MotherShip player;
	
	public turnSort(MotherShip player){
		this.player = player;
	}

	@Override
	public int compare(Move a, Move b) {
		if(a.getShip() == player) return -1;
		else if(b.getShip() == player) return 1;
		return 0;
	}

}
