package entities;
import graphics.ShipShape;

import java.util.ArrayList;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TransformGroup;

import SpaceGame.UniverseBuilder;
import behaviours.DeathBehaviour;
import behaviours.FlyBehaviour;

public abstract class Ship implements Observable{
	private Boolean alive;
	private String name;
	
	// Observer pattern
	protected ArrayList<Position> positions;
	
	// Position
	private int x;
	private int y;
	private int z;
	
	// Graphics
	private ShipShape shape;
	protected static float TF_SCALE;
	protected static float SIZE_SCALE = 0.75f;
	protected BranchGroup brGroup;
	protected UniverseBuilder universe;
	
	// Strategy pattern
	private DeathBehaviour deathBeaviour;
	private FlyBehaviour flyBehaviour;

	public Ship(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s){
		this.positions = watchers;
		this.setAlive(true);
		this.setX(x);
		this.setY(y);
		this.setZ(0);
		Ship.TF_SCALE = s;
		this.setUniverse(universe);
	}
	
	// Observable interface implementation
	@Override
	public void notifyObservers() {
		/*
		 * Tells all the positions squares the latest ship position
		 * Tells the universe locale whether the ship is alive or not
		 * If its alive it will be rendered, if not it will not be rendered
		 */
		for(Position p: this.positions){
			p.update(this);
		}
		universe.update(this);
	}
	
	// Move the ship
	public void move(String mv) {
		/*
		 * The board contains positions 0,0 to MAXX, MAXY
		 * Each ship occupies a position on the board
		 * This position is independent of the ship's rendered position on screen.
		 */
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
		this.flyBehaviour.fly(this);
	}
	
	public BranchGroup shipBranchGroup() {
		/*
		 * Creates a Branch Group for rendering a ship in the scene
		 */
		setBrGroup(new BranchGroup());
		getBrGroup().setCapability(BranchGroup.ALLOW_DETACH);// so that we can remove it from the scene later
		getBrGroup().setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		TransformGroup trGroup = new TransformGroup();
		
		// Set the capability bit so that we can move this ship after it is live
		trGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);	// so that we can modify it
		trGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);		// so that we can add more transformations
		trGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);		// So we can get the mesh		
		
		// Add the trandformGroup to the root branch
		getBrGroup().addChild(trGroup);
		
		// Get and reset the transform for the ship - determines where the ship is placed in space
//		Transform3D translate = this.getFlyBehaviour().moveShape(this);
		
//		trGroup.setTransform(translate);
		
		
		// Get the mesh for the ship
		ShipShape structure = this.getShape();
		Shape3D shipMesh = structure.getMesh();
		
		// Add the mesh to the transform group
		trGroup.addChild(shipMesh);
		
		this.flyBehaviour.fly(this);
		// The branchgroup will be added to the scene and is rendered as this ships mesh 
		// and all of its movements, animations, textures etc.
		return getBrGroup();
	}
	
	public void die() {
		this.setAlive(false);
		this.notifyObservers();
	}
	
	//============================================Getters and Setters=============================
	public Boolean isAlive() {
		return this.alive;
	}
	public void setAlive(Boolean alive) {
		this.alive = alive;
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
	
	public ShipShape getShape() {
		return shape;
	}

	public void setShape(ShipShape shape) {
		this.shape = shape;
	}

	public BranchGroup getBrGroup() {
		return brGroup;
	}

	void setBrGroup(BranchGroup brGroup) {
		this.brGroup = brGroup;
	}

	UniverseBuilder getUniverse() {
		return universe;
	}

	void setUniverse(UniverseBuilder universe) {
		this.universe = universe;
	}

	public DeathBehaviour getDeathBeaviour() {
		return deathBeaviour;
	}

	public void setDeathBeaviour(DeathBehaviour deathBeaviour) {
		this.deathBeaviour = deathBeaviour;
	}

	public FlyBehaviour getFlyBehaviour() {
		return flyBehaviour;
	}

	public void setFlyBehaviour(FlyBehaviour flyBehaviour) {
		this.flyBehaviour = flyBehaviour;
	}
}
