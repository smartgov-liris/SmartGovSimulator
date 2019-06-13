package smartgov.core.agent.moving;

/**
 * A structure that represents a parking area, in the meaning
 * of a place where agents can be stored outside of the graph.
 *
 * <p>
 * Does not necessarily represents an actual parking spot. The 
 * real purpose of the interface is to represent a structure where 
 * agents can wait (and act) outside of the global graph where other
 * agents are moving.
 * </p>
 * 
 * <p>
 * As an example, let's consider buses that wait a given amount of 
 * time at each stop.
 * </p>
 *
 * <p>
 * In a first situation, we could just make the bus <code>WAIT</code> on the node that
 * represents the stop. In that case, all the buses and other vehicles 
 * behind the bus will be blocked, until it moves again. In some situation,
 * it will be the expected behavior.
 * </p>
 *
 * <p>
 * But we can also imagine a <em>non-blocking</em> stop, using the 
 * ParkingArea interface.
 * </p>
 *
 * <p>
 * In this second situation, we could create a dedicated structure 
 * for the stop, linked to the same node as before (notice that it can be the node itself,
 * if it implements this interface).
 * When the bus then reaches the node, it can <code>ENTER</code> the ParkingArea, <code>WAIT</code>,
 * and finally <code>LEAVE</code> the ParkingArea.
 * </p>
 *
 * <p>
 * In this case, while the bus <code>WAIT</code>, other vehicles can still move
 * normally in the graph.
 * </p>
 */
public interface ParkingArea {

	public void enter(MovingAgent agent);
	public void leave(MovingAgent agent);
	public int spaceLeft();

}
