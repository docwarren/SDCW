package SpaceGame;

import entities.Observer;
import entities.Ship;

import java.util.ArrayList;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.Locale;
import javax.media.j3d.PhysicalBody;
import javax.media.j3d.PhysicalEnvironment;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.View;
import javax.media.j3d.ViewPlatform;
import javax.media.j3d.VirtualUniverse;
import javax.vecmath.Vector3d;

public class UniverseBuilder extends Object implements Observer{
	private VirtualUniverse world;		// World for everything
	private Locale loc;					// Locale of the scene - i.e. the virtual objects
	private TransformGroup vpTrans;		// transform for the viewer
	private View view;					// the viewer itself
	private Canvas3D canvas;			// The canvas we put everything on
	private BranchGroup scene;		// The branchgroup of the scene
	private ArrayList<Ship> livingShips = new ArrayList<Ship>();
	
	// Singleton ====================================== Constructor ==================================
	static UniverseBuilder uniqueInstance;
	
	public static synchronized UniverseBuilder getInstance(Canvas3D canvas) throws Exception_MC{
		if( uniqueInstance == null){
			uniqueInstance = new UniverseBuilder(canvas);
			return uniqueInstance;
		}
		else{
			return uniqueInstance;
		}		
	}
	
	private UniverseBuilder(Canvas3D canvas) {
		this.setCanvas(canvas);
		
		/*
		 * Creates a Java3D Virtual Universe for adding ships, board, background etc.
		 * FOllowing instructions here:
		 * https://docs.oracle.com/cd/E17802_01/j2se/javase/technologies/desktop/java3d/forDevelopers/j3dguide/Intro.doc.html#47363
		 */
		// Create universe with locale attached
		world = new VirtualUniverse();
		loc = new Locale(world);

		// Create Physical Body and PhysicalEnvironment
		PhysicalBody body = new PhysicalBody();
		PhysicalEnvironment env = new PhysicalEnvironment();
		
		// Create a view and attach Canvas
		view = new View();
		view.addCanvas3D(canvas);
		view.setPhysicalBody(body);
		view.setPhysicalEnvironment(env);
		
		// Create branchgroup
		BranchGroup vpRoot = new BranchGroup();
		
		// Create a ViewPlatform object
		Transform3D viewTranslate = translateView();
		ViewPlatform viewPlat = new ViewPlatform();
		vpTrans = new TransformGroup(viewTranslate);
		
		vpTrans.addChild(viewPlat);
		vpRoot.addChild(vpTrans);
		
		view.attachViewPlatform(viewPlat);
		
		loc.addBranchGraph(vpRoot);
	}
	
	public Transform3D translateView() {
		/*
		 * Creates a new Transform Group that moves the scene viewer back and up so that it can see all the ships
		 */
		Transform3D viewTranslate = new Transform3D();
		Transform3D viewRotate = new Transform3D();
		
		// Define where we want the view to view from
		float zPos = 20.0f;
		float yPos = 10.0f;
		float xPos = 0.0f;
		
		// Rotate first
		// Calculate the angle we need to rotate the view by so that it looks directly at 0,0,0
		double toRotate = Math.toRadians(90) - Math.atan(zPos/yPos);
		
		// Implement the rotation
		viewTranslate.rotX(-toRotate);
		viewRotate.rotY(0);
		viewTranslate.mul(viewRotate);
		
		// Then translate
		Vector3d trView = new Vector3d(xPos, yPos, zPos);
		viewTranslate.setTranslation(trView);
		
		return viewTranslate;
	}
	
	public void addBranchGraph(BranchGroup bg) {
        loc.addBranchGraph(bg);
    }

	public void removeBranch(BranchGroup brGroup) {
		loc.removeBranchGraph(brGroup);
	}
	
	public void addShipBranch(BranchGroup shipBranch){
		this.scene.addChild(shipBranch);
	}
	
	public void removeShipBranch(BranchGroup shipBranch){
		this.scene.removeChild(shipBranch);
	}

	@Override
	public void update(Ship ship) {
		if(ship.isAlive() && !this.livingShips.contains(ship)){
			this.livingShips.add(ship);
			this.addShipBranch(ship.getBrGroup());
		}
		else if(!ship.isAlive() && this.livingShips.contains(ship)) {
			this.livingShips.remove(ship);
			this.removeShipBranch(ship.getBrGroup());
		}
	}

	public Canvas3D getCanvas() {
		return canvas;
	}

	public void setCanvas(Canvas3D canvas) {
		this.canvas = canvas;
	}

	public BranchGroup getScene() {
		return scene;
	}

	public void setScene(BranchGroup scene) {
		this.scene = scene;
	}
}
