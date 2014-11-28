package SpaceGame;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.vecmath.Color3f;

public class Appearance_Passive extends Appearance {

	public Appearance_Passive() {
		// Set a material manually
		Color3f ambient = new Color3f(0.0f, 0.1f, 0.7f);
		Color3f diffuse = new Color3f(0.0f, 0.1f, 0.9f);
		Color3f specular = new Color3f(0.4f, 0.8f, 1.0f);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		float shiny = 10.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		this.setMaterial(material);
	}
}
