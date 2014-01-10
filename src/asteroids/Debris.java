package asteroids;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.Random;

/**
 * class representing debris
 * @author tkkemo
 *
 */
public class Debris extends Participant {
	//represents the outline
	private Shape outline;
	//used as a random number generator
	private Random random = new Random();
	
		//  Constructs either particle debris for asteroids, or lines for ship
		public Debris (int type, double x, double y) {
			Shape e;
			if (type == 1) {
				e = new Path2D.Double();
				((Path2D) e).moveTo(-20, 12);
				((Path2D) e).lineTo(-12, 0);
				setVelocity( random.nextDouble(), random.nextDouble());
			}
			
			else {
				e = new Ellipse2D.Double(0, 0, 1, 1);
				setVelocity(1 + random.nextDouble()*3, 1 + random.nextDouble()*3);
				}
			setPosition(x,y);
			setRotation(random.nextDouble() * 2*Math.PI);
			
			outline = e;
		}

		@Override
		Shape getOutline() {
			return outline;
		}
}
