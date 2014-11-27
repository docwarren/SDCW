package SpaceGame;
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
	private Controller_Collision cm;
	private String name;
	private int x;
	private int y;
	private int z;
	private ShipShape shape;
	private static float TF_SCALE;
	BranchGroup brGroup;

	public Ship(Controller_Collision cm, Position pos, float s){
		
		this.cm = cm;
		this.setX(pos.getX());
		this.setY(pos.getY());
		this.setZ(pos.getZ());
		Ship.TF_SCALE = s;
	}	
	
	@Override
	public void notifyObservers() {
		cm.update(this);		
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
		notifyObservers();
		updateShape();
	}
	
	public BranchGroup shipBranchGroup() {
		/*
		 * Creates a Branch Group for rendering a ship in the correct position
		 */
		brGroup = new BranchGroup();
		brGroup.setCapability(BranchGroup.ALLOW_DETACH);// so that we can remove it from the scene later
		brGroup.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		TransformGroup trGroup = new TransformGroup();
		
		// Set the capability bit so that we can move this ship after it is live
		trGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);	// so that we can modify it
		trGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);		// so that we can add more transformations
		trGroup.setCapability(TransformGroup.ALLOW_CHILDREN_READ);		// So we can get the mesh
		
		// Make an animator for the translation
		
		
		
		// Get the transform for the ship
		Transform3D translate = this.moveShape();
		trGroup.setTransform(translate);
		
		// Get the mesh for the ship
		ShipShape structure = this.getShape();
		Shape3D shipMesh = structure.getMesh();
		
		// Add the mesh to the transform group
		trGroup.addChild(shipMesh);
		
		// Add the transform to the branch
		brGroup.addChild(trGroup);
		return brGroup;
	}
	
	private void updateShape() {
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
		Vector3d vt = new Vector3d(sx, 0.0f, sz);
		translate.setTranslation(vt);
		
		return translate;
	}
	
	public Transform3D translation(){
		Transform3D translate = new Transform3D();
		float sx = (float)this.getX() * TF_SCALE - (TF_SCALE * 1.5f);
		float sz = (float)this.getY() * TF_SCALE - ( TF_SCALE * 2.0f);
		Vector3d vt = new Vector3d(sx, 0.0f, sz);
		translate.setTranslation(vt);
		return translate;
	}
	
	//============================================Getters and Setters=============================
	public Boolean isAlive() {
		return this.alive;
	}
	public void setAlive(Boolean alive) {
		this.alive = alive;
	}

	public Controller_Collision getCm() {
		return this.cm;
	}

	public void setMc(Controller_Collision cm) {
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
	
	public ShipShape getShape() {
		return shape;
	}

	public void setShape(ShipShape shape) {
		this.shape = shape;
	}
}
