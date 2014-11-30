package graphics;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class ShipShape_Player extends ShipShape {

	public ShipShape_Player() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Appearance getAppearance(){
		Appearance a = new Appearance();
		// Set a material manually
		Color3f ambient = rgbToCol3f(58, 96, 47);
		Color3f diffuse = rgbToCol3f(58, 96, 47);
		Color3f specular = rgbToCol3f(245, 255, 168);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		float shiny = 10.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}
}
