package asteroids;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

/**
 * represents the stars
 * @author Tim Knutson, CS1410 P10, 6 December 2012
 *
 */
public class Star extends Participant implements CountdownTimerListener {

	// The outline of the ship
		
	private Shape outline;
	private Random random = new Random();
	//the shape of the star
	Ellipse2D.Double e = new Ellipse2D.Double(0, 0, 1, 1);
	
		//  Constructs a star and calls the countDown timer some random interval between 0 and 7000 ms
		public Star (double x, double y) {
			setPosition(x,y);
			new CountdownTimer(this,this,random.nextInt(7000));
			
			outline = e;
		}

		@Override
		Shape getOutline() {
			return outline;
		}

		/**
		 * when called, if the star is on, it turns it off for 50 ms
		 * when off it turn the star on for a random time of
		 * between 2 and 7 seconds
		 */
		@Override
		public void timeExpired(Participant p) {
			if ( e.getHeight() == 1) {
				e.setFrame(e.getX(), e.getY(), 0, 0);
				new CountdownTimer(this,p,50);
			}
			else {
				e.setFrame(e.getX(), e.getY(), 1, 1);
				new CountdownTimer(this,p,2000 +random.nextInt(7000));
			}
		}
}