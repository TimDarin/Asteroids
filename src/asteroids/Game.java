package asteroids;

import javax.swing.*;
import java.awt.*;
import static asteroids.Constants.*;


/**
 * Implements an asteroid game.
 * @author Joe Zachary, modified by Tim Knutson, CS1410 P10, 6 December 2012.
 *
 */
public class Game extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel score;
	private JLabel lives;

	/**
	 * Launches the game
	 */
	public static void main (String[] args) {
		Game a = new Game();
		a.setVisible(true);
	}

	
	/**
	 * Lays out the game and creates the controller
	 */
	public Game () {
		
		// Title at the top
		setTitle(TITLE);
		
		// Default behavior on closing
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// The main playing area and the controller
		Screen screen = new Screen();
		Controller controller = new Controller(this, screen);

		// This panel contains the screen to prevent the screen from being resized
		JPanel screenPanel = new JPanel();
		screenPanel.setLayout(new GridBagLayout());
		screenPanel.add(screen);
		
		// This panel contains buttons and labels
		JPanel controls = new JPanel();
		
		// The button that starts the game
		JButton startGame = new JButton(START_LABEL);
		controls.add(startGame);
		
		//label for the score
		score = new JLabel("Score: " + controller.getScore());
		controls.add(score);
		
		//label for the lives
		lives = new JLabel("Lives: " + controller.getLives());
		controls.add(lives);
		
		// Organize everything
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(screenPanel, "Center");
		mainPanel.add(controls, "North");
		setContentPane(mainPanel);
		pack();
		
		// Connect the controller to the start button
		startGame.addActionListener(controller);
		
	}	
	
	/**
	 * lets the controller update the score
	 */
	public void setScore(int s) {
		score.setText("Score: " + s);
	}
	
	/**
	 * lets the controller update the number of lives
	 */
	public void setLives(int l) {
		lives.setText("Lives: " + l);
	}

}











		
