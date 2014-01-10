package asteroids;

/**
 * Must be implemented by objects wishing to receive notifications of
 * collisions.
 * @author Joe Zachary, modified by Tim Knutson, CS1410 P10, 6 December 2012.
 */
public interface CollisionListener {
	
	/**
	 * Reports that the two participants have collided
	 */
	public void collidedWith (Participant p1, Participant p2);
	
}
