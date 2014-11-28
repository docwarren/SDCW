package SpaceGame;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class ShipShape_Enemy extends ShipShape {

	public ShipShape_Enemy() {}
	
	@Override
	public Appearance getAppearance(){
		Appearance a = new Appearance();
		// Set a material manually
		Color3f ambient = new Color3f(0.7f, 0.1f, 0.0f);
		Color3f diffuse = new Color3f(0.9f, 0.1f, 0.0f);
		Color3f specular = new Color3f(1.0f, 0.8f, 0.4f);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		float shiny = 10.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}
}
