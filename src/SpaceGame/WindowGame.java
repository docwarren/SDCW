package SpaceGame;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.Group;
import javax.media.j3d.ImageComponent2D;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;

import entities.MotherShip;
import graphics.Square;

// A lot of cues taken from here:
// http://www.java3d.org/animationinteraction.html
// and much of my understanding comes from here:
// www.computing.northampton.ac.uk/~gary/csy3019/CSY3019SectionD.html

public class WindowGame extends Applet implements ActionListener{
	private static String ASSETDIR = "src/assets/";
	private static final long serialVersionUID = 1L;
	// User interface Variables
	private JButton mute = new JButton("Mute");
	private boolean soundIsMute = false;
	private JButton undo = new JButton("Undo");
	private JButton move = new JButton("Move");
	private JButton switchMode = new JButton("Switch Mode");
	private JButton quit = new JButton("Quit");
	
	// For 3D rendering
	UniverseBuilder universe;
	private Canvas3D canvas;
	private Text3D banner;
	
	// For music
	private static MusicPlayer ipod;
	
	// Backend game machine variables
	private MoveController mc;
	private static MotherShip player;
	
	// Start the game
	public static void main(String[] args) throws Exception_MS, Exception_MC{
		WindowGame game = new WindowGame();
		//MainFrame frame = 
		new MainFrame(game, 1200, 680);
		ipod = new MusicPlayer();
		ipod.play();
	}	
	
	public WindowGame() throws Exception_MS, Exception_MC {
		/*
		 * Instantiates a new Window Space Game
		 */
		// Front end creation
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		universe = UniverseBuilder.getInstance(canvas);
		add("Center", canvas);
		
		JPanel panel = new JPanel();
		
		// Handle click events
		mute.addActionListener(this);
		quit.addActionListener(this);
		switchMode.addActionListener(this);
		undo.addActionListener(this);
		move.addActionListener(this);
		
		// Add the buttons to the UI
		panel.add(quit);
		panel.add(mute);
		panel.add(switchMode);
		panel.add(undo);
		panel.add(move);
		add("South", panel);
		
		// Configure the backend
		mc = MoveController.getInstance(universe);
		System.out.println(mc.getPositions().toString());
		player = (MotherShip)mc.getShipFactory().createShip("MotherShip");
		
		// Render the Ships
		BranchGroup scene = createScene();
		
		universe.addBranchGraph(scene);
		// So that we can update ship positions directly through the UniverseBuilder as an observer
		universe.setScene(scene);
		player.notifyObservers();
		
	}

	public BranchGroup createScene(){
		BranchGroup rootNode = new BranchGroup();
		rootNode.setCapability(BranchGroup.ALLOW_DETACH);
		rootNode.setCapability(Group.ALLOW_CHILDREN_READ);
		rootNode.setCapability(Group.ALLOW_CHILDREN_WRITE);
		rootNode.setCapability(Group.ALLOW_CHILDREN_EXTEND);
		// Add the banner
		TransformGroup textGroup = getBanner();
		rootNode.addChild(textGroup);
		
		TransformGroup lightGroup = lightGroup();
		TransformGroup board = boardGroup();
		
		// Add to the group
		rootNode.addChild(lightGroup);
		rootNode.addChild(board);
		
		// Add a background image
		Background bg = getSky();
		rootNode.addChild(bg);
		
		return rootNode;
	}

	private TransformGroup getBanner() {
		/*
		 * Get a banner for showing scores etc.
		 */
		// Font and text
		// Help from here: http://www.java2s.com/Code/Java/3D/RendersaJava3D3DTextobjectswithacustomextrusion.htm
	    Shape extrusionShape = new java.awt.geom.Line2D.Double(0, 0, 2, 0);

	    FontExtrusion fontEx = new FontExtrusion(extrusionShape);
		Font3D font = new Font3D(new Font("TlwgTypewriter", Font.BOLD, 14), fontEx);
		banner = new Text3D(font, "Space Wars!");
		// Allow it to be modified after compiling
		banner.setCapability(Text3D.ALLOW_STRING_WRITE);
		mc.setBanner(banner);// Tell the MC(Move Controller changes it when kills are made
		// Get a decent texture
		Appearance ap = bannerAppearance();
		Shape3D bannerShape = new Shape3D(banner, ap);
		TransformGroup textGroup = new TransformGroup();
		Transform3D textTf = new Transform3D();
		textTf.setTranslation(new Vector3d(-7.0f, 0.0f, -10.0f));
		textTf.setScale(0.1);
		textGroup.setTransform(textTf);
		textGroup.addChild(bannerShape);
		return textGroup;
	}
	
	private Appearance bannerAppearance(){
		Appearance a = new Appearance();
		// Set a material manually
		Color3f ambient = rgbToCol3f(173, 9, 0);
		Color3f diffuse = rgbToCol3f(255, 247, 133);
		Color3f specular = rgbToCol3f(239, 255, 168);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		float shiny = 40.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}
	
	// Find colours here: http://color.hailpixel.com
	// They come in rgb and hex so we convert them to Color3f with this method 
	private Color3f rgbToCol3f(int i, int j, int k) {
		return new Color3f((float)i / 256, (float)j / 256, (float)k / 256);
	}

	private Background getSky() {
		/*
		 * Creates a new background from an image I down-loaded from here:
		 * http://hdw.eweb4.com/search/galaxy/
		 * and here: http://apod.nasa.gov/apod/image/1411/TulipNebulaMetsavainio.jpg
		 */
		TextureLoader tl = new TextureLoader(ASSETDIR + "/space2.jpg", this);
		ImageComponent2D myBackGround = tl.getImage();
		Background bg = new Background();
		BoundingSphere worldBounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
		bg.setImage(myBackGround);
	    bg.setCapability(Background.ALLOW_IMAGE_WRITE);
	    bg.setApplicationBounds(worldBounds);
	    return bg;
	}

	private TransformGroup lightGroup() {
		/*
		 * Sets up the lighting for the game
		 */
		// Create a light group
		TransformGroup lights = new TransformGroup();
		
		// Create a Transform that moves the light up and to the right to create nice shadows on ships
		// Decide where we want the light to be
		Transform3D translate = new Transform3D();
		Vector3d lightPos = new Vector3d(4.0f, 5.0f, 2.0f);
		lights.setTransform(translate);			// Set the translation of the group
		
		// Create a white light that shines 30m from its origin
		Color3f lightColour = new Color3f(1.0f, 1.0f, 1.0f);
		BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 30.0);
		
		// Create a light direction that is opposite to where we moved it so that it points at 0,0,0
		Vector3f lightDirection = new Vector3f((float)-lightPos.getX(), (float)-lightPos.getY(), (float)-lightPos.getZ());
		DirectionalLight light = new DirectionalLight(lightColour, lightDirection);
		light.setInfluencingBounds(bounds);
		
		// Create an ambient light
		AmbientLight amb = new AmbientLight(new Color3f(0.0f, 0.0f, 0.0f));
		amb.setInfluencingBounds(bounds);
		
		// Add the light to the light group
		lights.addChild(light);
		lights.addChild(amb);
		
		return lights;
	}
	
	private TransformGroup boardGroup(){
		/*
		 * Creates the board for players to play on
		 */
		TransformGroup board = new TransformGroup();
		// MAXX by MAXY grid of squares
		for(int x = 0; x < mc.getMaxX(); x++){
			for(int y = 0; y < mc.getMaxY(); y++){
				TransformGroup sqGroup = new TransformGroup();
				// Each square is a square class
				Square square;
				// Every even numbered square is black
				if( ( x + y ) % 2 == 0) square = new Square("white");
				else square = new Square("black");
				
				// Put each square in the right place
				Transform3D moveTo = new Transform3D();
				float sx = ( x * mc.getScale() ) - (mc.getScale() * 1.5f);
				float sz = ( y * mc.getScale() ) - (mc.getScale() * 2.0f);
				float sy = (-0.3f);
				Vector3d pos = new Vector3d(sx, sy, sz);
				
				moveTo.setTranslation(pos);
				sqGroup.setTransform(moveTo);
				sqGroup.addChild(square.getSquare());
				board.addChild(sqGroup);
			}
		}
		return board;
	}

	private void undoMoves(){
		mc.undoMove();
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		/*
		 * Event listeners for quitting, switching between attack and defence, moving and undoing moves
		 */
		//System.out.println(e.toString());
		if(e.getSource() == quit){
			System.exit(0);
		}
		else if(!player.isAlive()) {
			banner.setString("GAME OVER!");
			return;
		}
		else if(e.getSource() == move){
			mc.executeTurn(player);
		}
		else if(e.getSource() == undo){
			System.out.println("UNDO");
			undoMoves();
		}
		else if(e.getSource() == switchMode){
			// Change the banner
			if(player.isAttacking()) banner.setString("Passive mode");
			else banner.setString("Aggressive mode");
			// We have to remove the player branchgroup from the Locale
			universe.removeBranch(player.getBrGroup());
			// Switch to the other mesh properties
			player.switchMode();
			// and add a new Version of it, now that the mesh has changed
			universe.addBranchGraph(player.shipBranchGroup());
		}
		else if(e.getSource() == mute){
			if(soundIsMute){
				ipod.play();
				soundIsMute = false;
				mute.setText("Mute");
			}
			else{
				ipod.stop();
				soundIsMute = true;
				mute.setText("Play");
			}
		}
	}
}
