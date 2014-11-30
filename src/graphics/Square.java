package graphics;

import javax.media.j3d.Appearance;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Material;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;

import com.sun.j3d.utils.geometry.Box;

public class Square {
	
	private Box box;
	Color3f ambient;
	Color3f diffuse;
	Color3f specular;
	private Appearance a;
	
	public Square(String colour){
		if(colour.equals("white")){
			this.ambient = new Color3f(1.0f, 1.0f, 1.0f);
			this.diffuse = new Color3f(1.0f, 1.0f, 1.0f);
			this.specular = new Color3f(1.0f, 1.0f, 1.0f);
		}
		else{
			this.ambient = new Color3f(0.1f, 0.1f, 0.1f);
			this.diffuse = new Color3f(0.1f, 0.1f, 0.1f);
			this.specular = new Color3f(0.1f, 0.1f, 0.1f);
		}
		a = getAppearance();
		box = new Box(1.8f, 0.05f, 1.8f, a);
	}
	
	public Appearance getAppearance(){
		Appearance a = new Appearance();
		
		// Make it slightly transparent
		// From here: http://www.vrupl.evl.uic.edu/LabAccidents/java3d/lesson07/index.html
		TransparencyAttributes ta = new TransparencyAttributes();
		ta.setTransparencyMode(ta.BLENDED);
		ta.setTransparency(0.28f);
		a.setTransparencyAttributes(ta);		
		
		// Set the colours
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		// And the shininess
		float shiny = 100.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}

	public Box getSquare() {
		return box;
	}

	public void setSquare(Box box) {
		this.box = box;
	}
}
