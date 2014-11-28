package SpaceGame;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class Appearance_Aggressive extends Appearance{

	public Appearance_Aggressive() {
		// Set a material manually
		Color3f ambient = new Color3f(0.0f, 0.0f, 0.2f);
		Color3f diffuse = new Color3f(0.3f, 0.5f, 0.2f);
		Color3f specular = new Color3f(0.9f, 1.0f, 0.6f);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		float shiny = 10.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		this.setMaterial(material);
	}

}
