package behaviours;

import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.vecmath.Vector3d;

import entities.Ship;

public class ShipFlight implements FlyBehaviour {

	protected static float TF_SCALE = 3.6f;
	protected static float SIZE_SCALE = 0.75f;
	
	@Override
	public void fly(Ship ship) {
		/*
		 * This updates the graph scene node with new Translation for this entity
		 */
		TransformGroup thisGroup = (TransformGroup)ship.getShape().getMesh().getParent();
		// Add the current transform Node to the branch group
		Transform3D mv = moveShape(ship);
        thisGroup.setTransform(mv);                                                                                                                                                                                                                            
	}

	public Transform3D moveShape(Ship ship){
		/*
		 * Gets the current transform for this Ship based on its board position
		 */                                                                                                                                                                                                                                                                                                                                                                         
		Transform3D translate = new Transform3D();
		Transform3D rotate = new Transform3D();
		
		// Rotate first
		translate.rotY(Math.PI/2);
		rotate.rotX(0.0);
		translate.mul(rotate);
		
		// then translate
		float sx = (float)ship.getX() * TF_SCALE - (TF_SCALE * 1.5f);
		float sz = (float)ship.getY() * TF_SCALE - ( TF_SCALE * 2.0f);
		float sy = (float)ship.getZ() * 0.6f;
		Vector3d vt = new Vector3d(sx, sy, sz);
		translate.setTranslation(vt);
		translate.setScale(SIZE_SCALE);
		
		return translate;
	}
}
