package graphics;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class ShipShape_Cruizer extends ShipShape {

	public ShipShape_Cruizer() {}
	
	@Override
	public Appearance getAppearance(){
		Appearance a = new Appearance();
		// Set a material manually
		Color3f ambient = rgbToCol3f(0, 56, 82);
		Color3f diffuse = rgbToCol3f(59, 79, 60);
		Color3f specular = rgbToCol3f(238, 235, 255);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);

		float shiny = 20.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}
}
