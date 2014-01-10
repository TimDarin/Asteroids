package asteroids;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.*;


/**
 * Represents ship objects
 * @author Joe Zachary, modified by Tim Knutson, CS1410 P10, 6 December 2012
 */
public class Ship extends Participant {
	
	// The outline of the ship
	
	private Shape outline;
	
	private boolean thrust;
	Path2D.Double poly;
	private boolean appended;
	//  Constructs a ship
	public Ship () {
		appended = false;
		thrust = false;
		poly = new Path2D.Double();
		poly.moveTo(20, 0);
		poly.lineTo(-20, 12);
		poly.lineTo(-12, 0);
		poly.lineTo(-20, -12);
		poly.closePath();
		outline = poly;
	}
	
	public void setOppositeThrust () {
		if (thrust)
			thrust = false;
		else 
			thrust = true;
	}
	
	public void setThrust (boolean b) {
		thrust = b;
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
	
	/**
	 * Customizes the base move method by imposing friction
	 */
	@Override
	public void move () {
		super.move();
		friction();
	}
	
	@Override
	public void draw(Graphics2D g) {
		Path2D.Double thruster = new Path2D.Double();
		thruster.moveTo(-16, 6);
		thruster.lineTo(-24, 0);
		thruster.lineTo(-16, -6);
		if (thrust && appended == false) {
			poly.append(thruster.getPathIterator(null), false);
			appended = true;
			outline = poly;
		}
		else if (!thrust){
			poly = new Path2D.Double();
			poly.moveTo(20, 0);
			poly.lineTo(-20, 12);
			poly.lineTo(-12, 0);
			poly.lineTo(-20, -12);
			poly.closePath();
			appended = false;
			outline = poly;
		}
		super.draw(g);
	}
	
}