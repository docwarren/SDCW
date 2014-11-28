package SpaceGame;

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

public class UniverseBuilder extends Object {
	VirtualUniverse world;
	Locale loc;
	TransformGroup vpTrans;
	View view;
	Canvas3D canvas;
	
	public UniverseBuilder(Canvas3D canvas) {
		this.canvas = canvas;
		
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
}
