package SpaceGame;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.media.j3d.Appearance;
import javax.media.j3d.IndexedTriangleArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.vecmath.Color3f;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3f;

public abstract class ShipShape{
	private ArrayList<Point3f> vertices;
	private ArrayList<Vector3f> normals;
	private ArrayList<Integer> faceIndices;
	private ArrayList<Integer> normalIndices;
	Point3f[] vertexArray;
	Vector3f[] normalArray;
	int[] faceIndArray;
	int[] normIndArray;
	IndexedTriangleArray ship;
	Shape3D shipShape;
	
	public ShipShape(){	
		vertices = new ArrayList<Point3f>();
		normals = new ArrayList<Vector3f>();
		faceIndices = new ArrayList<Integer>();
		normalIndices = new ArrayList<Integer>();
		this.processFile("assets/SS2.obj");		
		
		ship = new IndexedTriangleArray(normals.size(),IndexedTriangleArray.NORMALS|IndexedTriangleArray.COORDINATES, faceIndices.size());
		ship.setCoordinates(0, vertexArray);
		ship.setCoordinateIndices(0, faceIndArray);
		
		ship.setNormals(0, normalArray);
		ship.setNormalIndices(0, normIndArray);
		
		Appearance a = getAppearance();
		shipShape = new Shape3D(ship, a);
	}
	
	public Shape3D getMesh(){
		return shipShape;
	}
	
	public Appearance getAppearance(){
		Appearance a = new Appearance();
		// Set a material manually
		Color3f ambient = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f diffuse = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f specular = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f emissive = new Color3f(1.0f, 0.0f, 0.0f);
		float shiny = 20.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}
	
	public void processFile(String f){
		try{
			FileReader fr = new FileReader(f);
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null){
				String[] coords = line.split(" ");
				if(coords[0].equals("v")){
					Point3f p = new Point3f(Float.parseFloat(coords[1]), Float.parseFloat(coords[2]), Float.parseFloat(coords[3]));
					vertices.add(p);
				}
				else if(coords[0].equals("vn")){
					Vector3f v = new Vector3f(Float.parseFloat(coords[1]), Float.parseFloat(coords[2]), Float.parseFloat(coords[3]));
					normals.add(v);
				}
				else if(coords[0].equals("f")){
					String[] xs = coords[1].split("/");
					String[] ys = coords[2].split("/");
					String[] zs = coords[3].split("/");
					int x = Integer.parseInt(xs[0]) - 1;
					int y = Integer.parseInt(ys[0]) - 1;
					int z = Integer.parseInt(zs[0]) - 1;
					int xn = Integer.parseInt(xs[2]) - 1;
					int yn = Integer.parseInt(ys[2]) - 1;
					int zn = Integer.parseInt(zs[2]) - 1;
					faceIndices.add(x);
					faceIndices.add(y);
					faceIndices.add(z);
					normalIndices.add(xn);
					normalIndices.add(yn);
					normalIndices.add(zn);
				}
			}
		}
		catch(IOException err){
			System.out.println(err.toString());
		}
		
		// Make the faces vertices and normals into arrays instead of ArrayLists
		vertexArray = new Point3f[vertices.size()];
		for(int i = 0; i < vertices.size(); i++){
			vertexArray[i] = vertices.get(i);
		}
		
		faceIndArray = new int[faceIndices.size()];
		for(int i = 0; i < faceIndices.size(); i++){
			faceIndArray[i] = faceIndices.get(i);
		}
		
		normalArray = new Vector3f[normals.size()];
		for(int i = 0; i < normals.size(); i++){
			normalArray[i] = normals.get(i);
			
		}
		normIndArray = new int[normalIndices.size()];
		for(int i = 0; i < normalIndices.size(); i++){
			normIndArray[i] = normalIndices.get(i);
		}
	}
}