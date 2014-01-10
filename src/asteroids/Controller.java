package asteroids;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.*;
import static asteroids.Constants.*;

/**
 * Controls a game of asteroids
 * @author Joe Zachary, modified by Tim Knutson, CS1410 P10, 6 December 2012.
 */
public class Controller implements CollisionListener, ActionListener, KeyListener, CountdownTimerListener {
	
	// Shared random number generator
	private Random random;
	
	// The ship (if one is active) or null (otherwise)
	private Ship ship;
	
	// When this timer goes off, it is time to refresh the animation
	private Timer refreshTimer; 
	
	// Count of how many transitions have been made.  This is used to keep two
	// conflicting transitions from being made at almost the same time.
	private int transitionCount;
	
	// Number of lives left, and score accumulation
	private int lives;
	private int score;
	
	//Keeps track of the number of Asteroids for levels
	private int asteroids;
	
	private Screen screen;
	//Passed to controller when constructed, used to communicate lives and score to the game for display
	private Game g;
	
	//Keep track of key presses
	private boolean leftPressed;
	private boolean rightPressed;
	private boolean upPressed;
	private boolean spacePressed;
	private boolean bulletLimiter;
	
	
	//to keep track of whether a round has been initiated
	private boolean round2;
	private boolean round3;
	private boolean round4;
	private boolean gameWon;
	
	//an array of 8 bullets that can be fired at once
	Bullet [] clip = new Bullet[8];
	
	//keeps track of the number of bullets fired
	private int bulletIndex;
	
	
	/**
	 * Constructs a controller to coordinate the game and screen
	 */
	public Controller (Game game, Screen screen) {
		g = game;
		this.screen = screen;
		
		// Initialize the random number generator
		random = new Random();
		
		// Set up the refresh timer.
		refreshTimer = new Timer(FRAME_INTERVAL, this);
		transitionCount = 0;
		
		asteroids = 161;
		score = 0;
		bulletIndex = 0;
		
		// Bring up the splash screen and start the refresh timer
		splashScreen();
		refreshTimer.start();
		
	}

	
	/**
	 * Configures the game screen to display the splash screen
	 */
	private void splashScreen () {
		
		// Clear the screen and display the legend
		screen.clear();
		screen.setLegend("Love you!!");
		
		// Place four asteroids near the corners of the screen.
		placeAsteroids();
		
		createStars(75);
		// Make sure there's no ship
		ship = null;
		
	}
	

	
	/**
	 * Set things up and begin a new game.
	 */
	private void initialScreen () {
		
		// Clear the screen
		screen.clear();
		
		// Place four asteroids
		placeAsteroids();
		
		// Place the ship
		placeShip();
		
		
		// Reset statistics
		g.setLives(lives);
		
		// Start listening to events.  In case we're already listening, take
		// care to avoid listening twice.
		screen.removeCollisionListener(this);
		screen.removeKeyListener(this);
		screen.addCollisionListener(this);
		screen.addKeyListener(this);
	
		
		// Give focus to the game screen
		screen.requestFocusInWindow();
		
		createStars(50);
		
	}
	
	
	/**
	 * The game is over.  Displays a message to that effect and
	 * enables the start button to permit playing another game.
	 */
	private void finalScreen () {
		screen.setLegend(GAME_OVER);
		asteroids = 161;
		round2 = false;
		round3 = false;
		round4 = false;
		gameWon = false;
		screen.removeCollisionListener(this);
		screen.removeKeyListener(this);
	}
	
	
	

	
	/**
	 * when called it passed location data, as well as the type of debris
	 * to be created. 1 is ship debris and 0 is asteroid debris
	 */
	public void createDebris(double x, double y, int type) {
		if (type == 0) {
			for (int i = 0; i < 5; i++) {
				Debris d = new Debris(3, x, y);
				screen.addParticipant(d);
				new CountdownTimer(this, d, 2000);
			}
		}
		if (type == 1) {
			for (int i = 0; i < 3; i++) {
				Debris d = new Debris(1, x, y);
				screen.addParticipant(d);
				new CountdownTimer(this, d, 2000);
			}
		}
	}

	/**
	 * creates amount number of stars in a random locations in the 
	 * background, moving at variable speeds all in the same 
	 * random direction.
	 */
	public void createStars (int amount) {
		double r= random.nextDouble()*Math.PI*2;
		
		for (int i = 0; i < amount; i++)
		{
			Participant a = new Star(random.nextDouble()*SIZE, random.nextDouble()*SIZE);
			a.setVelocity(.3 + random.nextDouble()* .2, r);
			screen.addParticipant(a);
		}
	}
	
	
	/**
	 * Places asteroids near the corners of the screen.
	 * Gives them random velocities and rotations.
	 */
	private void placeAsteroids () {
		//if initial large asteroid speed is 3, for round 2 is 4, and round 3 and 4 it is 5
		int speed = 3;
		if (asteroids < 134)
			speed = 4;
		if (asteroids < 99)
			speed = 5;
		
		Participant a = new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET);
		a.setVelocity(speed, random.nextDouble()*2*Math.PI);
		a.setRotation(2*Math.PI * random.nextDouble());
		screen.addParticipant(a);
		
		a = new Asteroid(1, 2, SIZE-EDGE_OFFSET, EDGE_OFFSET);
		a.setVelocity(speed, random.nextDouble()*2*Math.PI);
		a.setRotation(2*Math.PI * random.nextDouble());
		screen.addParticipant(a);
		
		a = new Asteroid(2, 2, EDGE_OFFSET, SIZE-EDGE_OFFSET);
		a.setVelocity(speed, random.nextDouble()*2*Math.PI);
		a.setRotation(2*Math.PI * random.nextDouble());
		screen.addParticipant(a);
		
		a = new Asteroid(3, 2, SIZE-EDGE_OFFSET, SIZE-EDGE_OFFSET);
		a.setVelocity(speed, random.nextDouble()*2*Math.PI);
		a.setRotation(2*Math.PI * random.nextDouble());
		screen.addParticipant(a);
		
		//if round 2 or above creates an additional asteroid in the bottom center
		if (asteroids < 134) {
			a = new Asteroid(3, 2, SIZE/2, SIZE-EDGE_OFFSET);
			a.setVelocity(speed, random.nextDouble()*2*Math.PI);
			a.setRotation(2*Math.PI * random.nextDouble());
			screen.addParticipant(a);
		}
		//if round 3 or above creates additional asteroid in the top center
		if (asteroids < 99) {
			a = new Asteroid(0, 2, SIZE/2, EDGE_OFFSET);
			a.setVelocity(speed, random.nextDouble()*2*Math.PI);
			a.setRotation(2*Math.PI * random.nextDouble());
			screen.addParticipant(a);
		}
		//if round 4 creates two additional asteroids on either side of the ship
		if (asteroids < 57) {
			a = new Asteroid(1, 2, EDGE_OFFSET, SIZE/2);
			a.setVelocity(speed, random.nextDouble()*2*Math.PI);
			a.setRotation(2*Math.PI * random.nextDouble());
			screen.addParticipant(a);
			
			a = new Asteroid(2, 2, SIZE - EDGE_OFFSET, SIZE/2);
			a.setVelocity(speed, random.nextDouble()*2*Math.PI);
			a.setRotation(2*Math.PI * random.nextDouble());
			screen.addParticipant(a);
		}
		
	}
	
	/**
	 * Place a ship in the center of the screen.
	 */
	private void placeShip () {
		if (ship == null) {
			ship = new Ship();
		}
		ship.setPosition(SIZE/2, SIZE/2);
		ship.setRotation(-Math.PI/2);
		ship.setVelocity(0, 0);
		screen.addParticipant(ship);
	}

	
	
	
	/**
	 * Deal with collisions between participants.
	 */
	@Override
	public void collidedWith(Participant p1, Participant p2) {
		if (p1 instanceof Asteroid && p2 instanceof Ship) {
			asteroidCollision((Asteroid)p1);
			shipCollision((Ship)p2);
		}
		else if (p1 instanceof Ship && p2 instanceof Asteroid) {
			asteroidCollision((Asteroid)p2);
			shipCollision((Ship)p1);
		}
		else if (p1 instanceof Bullet && p2 instanceof Asteroid) {
			asteroidCollision((Asteroid)p2);
			bulletCollision((Bullet)p1);
		}
		else if (p1 instanceof Asteroid && p2 instanceof Bullet) {
			bulletCollision((Bullet)p2);
			asteroidCollision((Asteroid)p1);
		}
//		else if (p1 instanceof Star && p2 instanceof Asteroid) {
//			starCollision((Star)p1);
//		}
//		else if (p1 instanceof Asteroid && p2 instanceof Star) {
//			starCollision((Star)p2);
//		}
	}
	
	
	/**
	 * Something has hit an asteroid
	 */
	private void asteroidCollision (Asteroid a) {
		
		// The asteroid disappears
		screen.removeParticipant(a);
		createDebris(a.getX(), a.getY(), 0);
		asteroids--;
		
		if (a.getSize() == 2)
			score += 20;
		if (a.getSize() == 1)
			score+= 50;
		if (a.getSize() == 0)
			score+=100;
		
		g.setScore(score);
		// Create two smaller asteroids.  Put them at the same position
		// as the one that was just destroyed and give them a random
		// direction. speed is 1 more than the larger asteroid
		int size = a.getSize();
		size = size - 1;
		if (size >= 0) {
			double speed = a.getVelocity() + 1;
			Asteroid a1 = new Asteroid(random.nextInt(4), size, a.getX(), a.getY());
			Asteroid a2 = new Asteroid(random.nextInt(4), size, a.getX(), a.getY());
			a1.setVelocity(speed, random.nextDouble()*2*Math.PI);
			a2.setVelocity(speed, random.nextDouble()*2*Math.PI);
			a1.setRotation(2*Math.PI*random.nextDouble());
			a2.setRotation(2*Math.PI*random.nextDouble());
			screen.addParticipant(a1);
			screen.addParticipant(a2);
		}
	}
	
	/**
	 * The ship has collided with something
	 */
	private void shipCollision (Ship s) {
		
		// Remove the ship from the screen and null it out
		screen.removeParticipant(s);
		createDebris(ship.getX(), ship.getY(), 1);
		ship = null;
		// Display a legend and make it disappear in one second
		screen.setLegend("Ouch!");
		new CountdownTimer(this, null, 1000);
		
		// Decrement lives
		lives--;
		g.setLives(lives);
		// Start the timer that will cause the next round to begin.
		new TransitionTimer(END_DELAY, transitionCount, this);
		
	}
	
	/**
	 * bullet dissapears when hitting an asteroid
	 */
	private void bulletCollision (Bullet b) {
		screen.removeParticipant(b);
	}
	

	
	
	/**
	 * This method will be invoked because of button presses and timer events.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// The start button has been pressed.  Stop whatever we're doing
		// and bring up the initial screen
		if (e.getSource() instanceof JButton) {
			transitionCount++;
			asteroids = 161;
			score = 0;
			g.setScore(score);
			lives = 3;
			initialScreen();	

			round2 = false;
			round3 = false;
			round4 = false;
			gameWon = false;
			
			
		}
		if (asteroids == 133 && !round2) {
			round2 = true;
			transitionCount++;
			new TransitionTimer(END_DELAY, transitionCount, this);
		}
		if (asteroids == 98 && !round3) {
			round3 = true;
			transitionCount++;
			new TransitionTimer(END_DELAY, transitionCount, this);
		}
		if (asteroids == 56 && !round4) {
			round4 = true;
			transitionCount++;
			new TransitionTimer(END_DELAY, transitionCount, this);
		}
		if (asteroids == 0 && !gameWon) {
			gameWon = true;
			transitionCount++;
			screen.setLegend("YOU WIN!");
			
			screen.removeParticipant(ship);
			leftPressed = false;
			rightPressed = false;
			upPressed = false;
			spacePressed = false;
			asteroids = 105;
			round2 = false;
			round3 = false;
			gameWon = false;
			
			screen.removeCollisionListener(this);
			screen.removeKeyListener(this);
		}
		
		//if you reach the end of round 2 without dying, you get an extra life.
		if (score == 4680 && lives == 3) {
			lives++;
		}
		
		// Time to refresh the screen
		else if (e.getSource() == refreshTimer) {
			
			if (ship != null) {
				if (spacePressed && !bulletLimiter && bulletIndex < 8) {
					bulletLimiter = true;	
					clip[bulletIndex] = new Bullet(ship.getXNose(), ship.getYNose(), ship.getRotation());
					screen.addParticipant(clip[bulletIndex]);
					new CountdownTimer(this, ship, 500);
					new CountdownTimer(this, clip[bulletIndex], 900);
					bulletIndex++;
				}
				if (rightPressed) {
					ship.rotate(Math.PI/16);
				}
				if (leftPressed) {
					ship.rotate(-Math.PI/16);
				}
				if (upPressed) {
					ship.accelerate(1.2);
					ship.setOppositeThrust();
				}
				else 
					ship.setThrust(false);
				
					ship.friction();
			}
				
			// Refresh screen
			screen.refresh();
		}
		
		
	}
	
	
	/**
	 * Get the number of transitions that have occurred.
	 */
	public int getTransitionCount () {
		return transitionCount;
	}
	
	/**
	 * Based on the state of the controller, transition to the next state.
	 */
	public void performTransition () {
		
		// Record that a transition was made.  That way, any other pending
		// transitions will be ignored.
		transitionCount++;
		
		// If there are no lives left, the game is over.  Show
		// the final screen.
		if (lives == 0) {
			finalScreen();
		}
		
		// The ship must have been destroyed.  Place a new one an
		// continue on the current level
		else if (asteroids == 133){
			initialScreen();
			screen.setLegend("Round 2!");
			new CountdownTimer(this, null, 1000);
		}
		else if (asteroids == 98){
			initialScreen();
			screen.setLegend("Round 3!");
			new CountdownTimer(this, null, 1000);
		}
		else if (asteroids == 56){
			initialScreen();
			screen.setLegend("Round 4!");
			new CountdownTimer(this, null, 1000);
		}
		else 
			placeShip();
		
	}


	/**
	 * Deals with certain key presses
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) 
			leftPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				rightPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_UP)
				upPressed = true;
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			spacePressed = true;
		
		//random teleport to random part of the screen with random rotation when t is pressed
		if (e.getKeyCode() == KeyEvent.VK_T && ship !=null) {
			ship.setPosition(random.nextDouble()*SIZE, random.nextDouble()*SIZE);
			ship.setRotation(random.nextDouble()*2*Math.PI);
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
				leftPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
				rightPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_UP)
				upPressed = false;
		if (e.getKeyCode() == KeyEvent.VK_SPACE) {
			spacePressed = false;
			bulletLimiter = false;
		}
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	/**
	 * Callback for countdown timer.  Used to create transient effects.
	 */
	@Override
	public void timeExpired(Participant p) {
		if (p == null)
			screen.setLegend("");
		if (p instanceof Ship) {
			bulletLimiter = false;
		}
		if (p instanceof Bullet) {
			screen.removeParticipant(p);
			bulletIndex--;
		}
		if (p instanceof Star) {
			//p.on();
		}
		if (p instanceof Debris) {
			screen.removeParticipant(p);
		}
	}
	
	/**
	 * returns the score
	 * @return
	 */
	public int getScore () {
		return score;
	}
	/**
	 * returns the number of lives
	 * @return
	 */
	public int getLives () {
		return lives;
	}

}
