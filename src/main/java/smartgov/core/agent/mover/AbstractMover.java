package smartgov.core.agent.mover;

import org.locationtech.jts.geom.Coordinate;

/**
 * AbstractMover describes the abstract behavior to move in a given structure
 * .
 * @author Simon
 *
 */
public abstract class AbstractMover {

	public abstract Coordinate moveOn(Coordinate destination);
	
	public abstract Coordinate moveOn(double timeInTicks);
	
}
