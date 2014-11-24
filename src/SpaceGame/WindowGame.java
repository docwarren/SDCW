package SpaceGame;

import java.applet.Applet;
import java.awt.*;

import javax.media.j3d.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;

// A lot of cues taken from here:
// http://www.java3d.org/animationinteraction.html
public class WindowGame extends Applet{
	private static final long serialVersionUID = 1L;
	private JButton undo = new JButton("Undo");
	private JButton move = new JButton("Move");
	private JButton switchMode = new JButton("Switch Mode");
	private JButton quit = new JButton("Quit");
	
	private TransformGroup shipTrans;
	private Transform3D trans = new Transform3D();
	private float height=0.0f;
	private float sign = 1.0f; // going up or down
	private Timer timer;
	private float xloc=0.0f;
	
	private CollisionManager cm;
	private MoveController mc;
	private MotherShip player;
	private ShipFactory shFactory;
	private MoveFactory mvFactory;
	
	public static void main(String[] args) throws MotherShipException, CollisionManagerException, MoveControllerException{
		WindowGame game = new WindowGame();
		MainFrame frame = new MainFrame(game, 1200, 680);
	}	
	
	public WindowGame() throws MotherShipException, CollisionManagerException, MoveControllerException{
		cm = CollisionManager.getInstance(4, 4);
		mc = MoveController.getInstance(4, 4);
		player = MotherShip.getInstance(cm);		// Create the mothership
		mc.addShip(player);							// Add the mothership to the game
		player.notifyObservers();					// Update the collision detector about the position of the new ship
		
		shFactory = new ShipFactory(cm);	
		mvFactory = new MoveFactory();
		String[] shipTypes = {"BattleCruizer", "BattleShooter", "BattleStar"};
		
		setLayout(new BorderLayout());
		GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
		Canvas3D canvas = new Canvas3D(config);
		add("Center", canvas);
		
		JPanel panel = new JPanel();
		panel.add(quit);
		panel.add(switchMode);
		panel.add(undo);
		panel.add(move);
		
		add("South", panel);
	}
	
	public BranchGroup createScene(){
		BranchGroup group = new BranchGroup();
		group.addChild(new Box());
		return null;
	}
}
