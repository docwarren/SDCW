package SpaceGame;
import java.util.ArrayList;

import javax.media.j3d.Alpha;
import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Node;
import javax.media.j3d.PositionInterpolator;
import javax.media.j3d.RotationInterpolator;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public abstract class Ship implements Observable{
	private Boolean alive;
	protected ArrayList<Position> positions;
	private String name;
	private int x;
	private int y;
	private int z;
	private ShipShape shape;
	private static float TF_SCALE;
	private static float SIZE_SCALE = 0.75f;
	protected BranchGroup brGroup;
	protected UniverseBuilder universe;

	public Ship(UniverseBuilder universe, ArrayList<Position> watchers,int x, int y, float s){
		this.positions = watchers;
		this.setAlive(true);
		this.setX(x);
		this.setY(y);
		this.setZ(0);
		Ship.TF_SCALE = s;
		this.setUniverse(universe);
	}
	
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
		 * The board contains positions 00 to 33
		 * Each ship occupies a position on the board
		 * This position is independent of the ship's rendered position on screen.
		 * The collision manager does not take account of the rendered position.
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
		updateShape();
		notifyObservers();
	}
	
	public BranchGroup shipBranchGroup() {
		/*
		 * Creates a Branch Group for rendering a ship in the correct position
		 * Animation help from here: http://www.computing.northampton.ac.uk/~gary/csy3019/CSY3019SectionD.html
		 */
		setBrGroup(new BranchGroup());
		getBrGroup().setCapability(BranchGroup.ALLOW_DETACH);// so that we can remove it from the scene later
		getBrGroup().setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		TransformGroup trGroup = new TransformGroup();
		
		// Set the capability bit so that we can move this ship after it is live
		trGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);	// so that we can modify it
		trGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);		// so that we can add more transformations
		trGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);		// So we can get the mesh
		
		// Make an animator for the translation
		Alpha alf = new Alpha(1, 3000);
		PositionInterpolator PI = new PositionInterpolator(alf, trGroup);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0f, 0.0f,0.0f), 100.0f);
		PI.setBounds(bounds);
		
		// Add the trandformGroup to the branch
		getBrGroup().addChild(trGroup);
		
		// Get and reset the transform for the ship
		Transform3D translate = this.moveShape();
		trGroup.setTransform(translate);
		
		// Get the mesh for the ship
		ShipShape structure = this.getShape();
		Shape3D shipMesh = structure.getMesh();
		
		// Add the mesh to the transform group
		trGroup.addChild(shipMesh);
		
		// Add the translation to the trGroup
		trGroup.addChild(PI);
		
		return getBrGroup();
	}
	
	public void updateShape() {
		/*
		 * This updates the graph scene node with new Translation for this entity
		 */
		TransformGroup thisGroup = (TransformGroup)this.getShape().getMesh().getParent();
		// Add the current transform Node to the branch group
		Transform3D mv = moveShape();
		thisGroup.setTransform(mv);
	}

	public Transform3D moveShape(){
		/*
		 * Gets the current transform for this Ship based on its board position
		 */
		Transform3D translate = new Transform3D();
		Transform3D rotate = new Transform3D();
		
		// Rotate first
		if(this.getName() == "MotherShip") translate.rotY(-Math.PI/2);
		else translate.rotY(Math.PI/2);
		rotate.rotX(0.0);
		translate.mul(rotate);
		
		// then translate
		float sx = (float)this.getX() * TF_SCALE - (TF_SCALE * 1.5f);
		float sz = (float)this.getY() * TF_SCALE - ( TF_SCALE * 2.0f);
		float sy = (float)this.getZ() * 0.6f;
		Vector3d vt = new Vector3d(sx, sy, sz);
		translate.setTranslation(vt);
		translate.setScale(SIZE_SCALE);
		
		return translate;
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

	BranchGroup getBrGroup() {
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
}
