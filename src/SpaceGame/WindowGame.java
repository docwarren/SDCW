package SpaceGame;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.media.j3d.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

// A lot of cues taken from here:
// http://www.java3d.org/animationinteraction.html
// and much of my understanding comes from here:
// www.computing.northampton.ac.uk/~gary/csy3019/CSY3019SectionD.html

public class WindowGame extends Applet implements ActionListener{
	private static final long serialVersionUID = 1L;
	// User interface Variables
	private JButton undo = new JButton("Undo");
	private JButton move = new JButton("Move");
	private JButton switchMode = new JButton("Switch Mode");
	private JButton quit = new JButton("Quit");
	
	
	// For 3D rendering
	private Canvas3D canvas;
	private VirtualUniverse world;
	private View view;
	private TransformGroup bigGroup;
	private Text3D banner;
	
	// Backend game machine variables
	private CollisionController cc;
	private MoveController mc;
	private static ShipMother player;
	private Factory_Ship shFactory;
	private Factory_Move mvFactory;
	private String[] shipTypes = {"BattleCruizer", "BattleShooter", "BattleStar"};
	private static float TF_SCALE = 3.6f;
	private static float SHIP_SCALE = 0.75f;
	private static int MAXX = 4;
	private static int MAXY = 4;
	
	// Start the game
	public static void main(String[] args) throws Exception_MS, Exception_CM, Exception_MC{
		WindowGame game = new WindowGame();
		MainFrame frame = new MainFrame(game, 1200, 680);
	}	
	
	public WindowGame() throws Exception_MS, Exception_CM, Exception_MC {
		/*
		 * Instantiates a new Window Space Game
		 */
		// Configuration
		backendConfigure();
		
		// Front end creation
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		canvas = new Canvas3D(config);
		add("Center", canvas);
		
		JPanel panel = new JPanel();
		
		// Handle click events
		quit.addActionListener(this);
		switchMode.addActionListener(this);
		undo.addActionListener(this);
		move.addActionListener(this);
		
		// Add the buttons to the UI
		panel.add(quit);
		panel.add(switchMode);
		panel.add(undo);
		panel.add(move);
		add("South", panel);
		
		// Render the Ships
		BranchGroup scene = createScene();
		UniverseBuilder universe = new UniverseBuilder(canvas);
		universe.addBranchGraph(scene);
	}
	
	private void backendConfigure() throws Exception_CM, Exception_MC, Exception_MS {
		/*
		 * Sets up the Collision Manager, MotherShip and MoveController Singletons
		 * Creates a Ship Factory and a Move Factory
		 */
		cc = CollisionController.getInstance(MAXX, MAXY);
		mc = MoveController.getInstance(MAXX, MAXY);
		player = ShipMother.getInstance(cc, TF_SCALE);		// Create the mothership
		mc.addShip(player); 								// Add the mothership to the game
		player.notifyObservers();							// Update the collision detector about the position of the new ship
		
		shFactory = new Factory_Ship(cc, TF_SCALE);
		mvFactory = new Factory_Move();
	}

	public BranchGroup createScene(){
		BranchGroup rootNode = new BranchGroup();
		
		// Add the banner
		TransformGroup textGroup = getBanner();
		rootNode.addChild(textGroup);
		
		// Create a transform group so we can move stuff around
		bigGroup = new TransformGroup();
		bigGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		bigGroup.setCapability(TransformGroup.ALLOW_CHILDREN_EXTEND);
		bigGroup.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
		
		BranchGroup shipGroup = player.shipBranchGroup();
		TransformGroup lightGroup = lightGroup();
		TransformGroup board = boardGroup();
		
		// Add to the group
		bigGroup.addChild(lightGroup);
		bigGroup.addChild(shipGroup);
		bigGroup.addChild(board);
		rootNode.addChild(bigGroup);
		
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
		Font3D font = new Font3D(new Font("SanSerif", Font.BOLD, 12), new FontExtrusion());
		banner = new Text3D(font, "Space Wars!");
		// Allow it to be modified after compiling
		banner.setCapability(Text3D.ALLOW_STRING_WRITE);
		// Get a decent texture
		Appearance ap = bannerAppearance();
		Shape3D bannerShape = new Shape3D(banner, ap);
		TransformGroup textGroup = new TransformGroup();
		Transform3D textTf = new Transform3D();
		textTf.setTranslation(new Vector3d(-5.0f, 0.0f, -10.0f));
		textTf.setScale(0.1);
		textGroup.setTransform(textTf);
		textGroup.addChild(bannerShape);
		return textGroup;
	}
	
	private Appearance bannerAppearance(){
		Appearance a = new Appearance();
		// Set a material manually
		Color3f ambient = new Color3f(1.0f, 0.9f, 0.9f);
		Color3f diffuse = new Color3f(1.0f, 0.9f, 0.9f);
		Color3f specular = new Color3f(1.0f, 1.0f, 1.0f);
		Color3f emissive = new Color3f(0.0f, 0.0f, 0.0f);
		float shiny = 40.0f;
		Material material = new Material(ambient, emissive, diffuse, specular, shiny);
		a.setMaterial(material);
		return a;
	}

	private Background getSky() {
		/*
		 * Creates a new background from an image I down-loaded from here:
		 * http://hdw.eweb4.com/search/galaxy/
		 * and here: http://apod.nasa.gov/apod/image/1411/TulipNebulaMetsavainio.jpg
		 */
		TextureLoader tl = new TextureLoader("assets/space2.jpg", this);
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
		for(int x = 0; x < MAXX; x++){
			for(int y = 0; y < MAXY; y++){
				TransformGroup sqGroup = new TransformGroup();
				Square square;
				if( ( x + y ) % 2 == 0) square = new Square("white");
				else square = new Square("black");
				
				Transform3D moveTo = new Transform3D();
				float sx = ( x * TF_SCALE ) - (TF_SCALE * 1.5f);
				float sz = ( y * TF_SCALE ) - (TF_SCALE * 2.0f);
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
		cc.undoDeaths(mc);
		mc.undoMove();
		//renderScene();
	}
	
	private void moveShips() throws Exception_MC, Exception_MS{
		// Move the MotherShip first
		String playerMoveType = mc.randomMove(player);
		Move pmv = mvFactory.createMove(player, playerMoveType);
		mc.getCurrentTurn().add(pmv);
		
		// Loop through all the positions
		for(Position p: cc.getPositions()){
			// For each ship create a new move
			for(Ship sh: p.getShips()){
				if(sh.getName() == "MotherShip") continue;
				String moveType = mc.randomMove(sh);			// Randomly generate a move type
				Move mv = mvFactory.createMove(sh, moveType);	// Create the appropriate move accordingly
				mc.getCurrentTurn().add(mv);					// Add the moves to the list of moves to be executed this turn
			}
		}
		
		mc.executeTurn();
		cc.resolveCollisions(mc, bigGroup, banner);
		mc.renderShips();
		
		// Randomly create a new ship;
		Random r = new Random();
		int x = r.nextInt(3);
		Ship newShip = null;
		if(x == 0){
			newShip = shFactory.createShip(shipTypes[r.nextInt(shipTypes.length)]);
			// Add the new ship to the game
			mc.addShip(newShip);
			bigGroup.addChild(newShip.shipBranchGroup());
			newShip.notifyObservers();	// Notify the collision manager about the new ship
		}
	}	
	
	@Override
	public void actionPerformed(ActionEvent e){
		if(!player.isAlive()) banner.setString("GAME OVER!");
		else if(e.getSource() == move){
			try {
				moveShips();
			} catch (Exception_MC | Exception_MS e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource() == undo){
			undoMoves();
		}
		else if(e.getSource() == quit){
			System.exit(0);
		}
		else if(e.getSource() == switchMode){
			// Change the banner
			if(player.isAttacking()) banner.setString("Passive mode");
			else banner.setString("Aggressive mode");
			// We have to remove the player branchgroup from the bigGroup
			bigGroup.removeChild(player.getShape().getMesh().getParent().getParent());
			// Switch to the other mesh properties
			player.switchMode();
			// and add a new Version of it, now that the mesh has changed
			bigGroup.addChild(player.shipBranchGroup());
		}
	}
}
