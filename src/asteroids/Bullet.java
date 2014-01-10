package asteroids;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.Random;

/**
 * class representing a bullet
 * @author Tim Knutson
 *
 */
public class Bullet extends Participant{

	// The outline of the ship
	
	private Shape outline;
	
		//  Constructs a ship
		public Bullet (double x, double y, double radians) {
			Ellipse2D.Double e = new Ellipse2D.Double(0, 0, 1, 1);
			setPosition(x,y);
			setVelocity(20, radians);
			outline = e;
		}

		@Override
		Shape getOutline() {
			return outline;
		}

}
