package SpaceGame;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class ShipShape_Aggressive extends ShipShape {

	public ShipShape_Aggressive() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Appearance getAppearance(){
		Appearance a = new Appearance();
		// Set a material manually
		Color3f ambient = new Color3f(0.0f, 1.0f, 0.0f);
		Color3f diffuse = new Color3f(0.0f, 1.0f, 0.0f);
		Color3f specular = new Color3f(0.0f, 1.0f, 0.0f);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		float shiny = 20.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}
}
