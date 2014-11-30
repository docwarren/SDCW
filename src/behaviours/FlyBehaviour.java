package behaviours;

import javax.media.j3d.Transform3D;

import entities.Ship;

public interface FlyBehaviour{
	public void fly(Ship ship);
	public Transform3D moveShape(Ship ship);
}
