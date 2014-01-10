package asteroids;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D.Double;

public class AlienShip extends Participant{

	private Shape outline;
	
	//  Constructs a ship
	public AlienShip () {
		Ellipse2D.Double e = new Ellipse2D.Double(0,0,40, 5);

		outline = e;
	}
	
	/**
	 * Returns the x-coordinate of the point on the screen where the 
	 * ship's nose is located.
	 */
	public double getXNose () {
		Point2D.Double point = new Point2D.Double(20, 0);
		transformPoint(point);
		return point.getX();
	}
	
	/**
	 * Returns the x-coordinate of the point on the screen where the
	 * ship's nose is located.
	 */
	public double getYNose () {
		Point2D.Double point = new Point2D.Double(20, 0);
		transformPoint(point);
		return point.getY();
	}
	
	
	/**
	 * Returns the outline of the ship.
	 */
	@Override
	protected Shape getOutline () {
		return outline;
	}
	
	

}
