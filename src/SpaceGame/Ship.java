package SpaceGame;

public abstract class Ship implements Observable{
	private Boolean alive;
	private CollisionManager cm;
	private String name;
	private int x;
	private int y;
	private int z;

	public Ship(CollisionManager cm, Position pos){
		this.cm = cm;
		this.setX(pos.getX());
		this.setY(pos.getY());
		this.setZ(pos.getZ());
	}	
	
	@Override
	public void notifyObservers() {
		cm.update(this);		
	}
	
	// Move the ship
	public void move(String mv) {
		if(mv.equals("U")) this.y--;
		else if(mv.equals("D")) this.y++;
		else if(mv.equals("L")) this.x--;
		else if(mv.equals("R")) this.x++;
		else if(mv.equals("UR")) {
			this.x++;
			this.y--;			
		}
		else if(mv.equals("DR")) {
			this.x++;
			this.y++;
		}
		else if(mv.equals("UL")) {
			this.x--;
			this.y--;
		}
		else if(mv.equals("DL")) {
			this.x--;
			this.y++;
		}
		notifyObservers();
	}
	
	//============================================Getters and Setter=============================
	public Boolean isAlive() {
		return this.alive;
	}
	public void setAlive(Boolean alive) {
		this.alive = alive;
	}

	public CollisionManager getCm() {
		return this.cm;
	}

	public void setMc(CollisionManager cm) {
		this.cm = cm;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getX() {
		return this.x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return this.y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return this.z;
	}

	public void setZ(int z) {
		this.z = z;
	}
}
